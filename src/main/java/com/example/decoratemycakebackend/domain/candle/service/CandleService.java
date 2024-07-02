package com.example.decoratemycakebackend.domain.candle.service;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCountPermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCreatePermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.*;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.candle.repsository.CandleRepository;
import com.example.decoratemycakebackend.domain.friend.service.FriendRequestService;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import com.example.decoratemycakebackend.global.s3.S3Service;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import com.example.decoratemycakebackend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleService {

    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;
    private final CandleRepository candleRepository;
    private final FriendRequestService friendRequestService;
    private final S3Service s3Service;

    private Member getMember(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (member.getDeleted()) {
            throw new CustomException(ErrorCode.MEMBER_DELETED);
        }
        return member;
    }

    public CandleListDto addCandle(CandleAddRequestDto requestDto) {
        // 로그인 한 멤버 정보 조회
        Member currentMember = getMember(SecurityUtil.getCurrentUserEmail());

        Member candleOwner = getMember(requestDto.getCakeOwnerEmail());

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getCakeOwnerEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        String imageUrl = s3Service.getImageUrl(requestDto.getCandleName().toString());

        // 케이크의 CandleCreatePermission 확인
        if (cake.getCandleCreatePermission() == CandleCreatePermission.ONLY_FRIENDS) {
            // 현재 사용자와 케이크 소유자가 친구 관계인지 확인
            if (!friendRequestService.isFriend(currentMember, candleOwner)) {
                throw new CustomException(ErrorCode.NOT_FRIEND);
            }
        }

        // 캔들 생성하기
        Candle candle = Candle.builder()
                .candleTitle(requestDto.getCandleTitle())
                .candleContent(requestDto.getCandleContent())
                .candleName(requestDto.getCandleName())
                .candleUrl(imageUrl)
                .writer(requestDto.getWriter())
                .writerEmail(currentMember.getEmail())
                .isPrivate(requestDto.isPrivate())
                .cake(cake)
                .candleCreatedAt(LocalDateTime.now())
                .build();

        Candle savedCandle = candleRepository.save(candle);
        cakeRepository.save(cake);

        // CandleListDto 생성 및 반환
        return CandleListDto.from(savedCandle);
    }

    public CandleResponseDto getCandleByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserEmail();
        Page<Candle> candlePage = candleRepository.findAllByWriterEmail(email, pageable);
        Page<CandleListDto> candleListDtoPage = CandleListDto.from(candlePage);
        long totalCandles = candlePage.getTotalElements();

        return new CandleResponseDto("캔들 조회가 완료되었습니다.", candleListDtoPage, totalCandles);
    }

    public CandleResponseDto getCandleByCake(CandleGetByCakeRequestDto requestDto, Pageable pageable) {
        Member currentMember = getMember(SecurityUtil.getCurrentUserEmail());
        Cake cake = getCake(requestDto);

        // 케이크 주인이 허용한 권한과 일치 여부 확인
        boolean canViewCandle = canViewCandle(cake, currentMember);
        // 캔들 개수 열람 허용 안되면 -1로 반환, 캔들 데이터는 미반환
        if (!canViewCandle) {
            String message = getViewCandleMessage(cake);
            return new CandleResponseDto(message, null, -1);
        }

        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        Page<CandleListDto> candleListDtoPage = CandleListDto.from(candlePage);

        long totalCandles = canViewCandleCount ? candlePage.getTotalElements() : -1; // 수정된 부분

        return new CandleResponseDto("캔들 조회가 완료되었습니다.", candleListDtoPage, totalCandles);
    }

    private Cake getCake(CandleGetByCakeRequestDto requestDto) {
        return cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));
    }

    private boolean canViewCandle(Cake cake, Member currentMember) {
        CandleViewPermission permission = cake.getCandleViewPermission();

        return switch (permission) {
            case ANYONE -> true;
            case ONLY_ME -> currentMember.equals(cake.getMember());
            case ONLY_FRIENDS -> friendRequestService.isFriend(currentMember, cake.getMember());
            default -> false;
        };
    }

    private boolean canViewCandleCount(Cake cake, Member currentMember) {
        CandleCountPermission permission = cake.getCandleCountPermission();

        return switch (permission) {
            case ANYONE -> true;
            case ONLY_ME -> currentMember.equals(cake.getMember());
            default -> false;
        };
    }

    private String getViewCandleMessage(Cake cake) {

        return switch (cake.getCandleViewPermission()) {
            case ONLY_ME -> "이 케이크는 케이크 주인만 볼 수 있습니다.";
            case ONLY_FRIENDS -> "이 케이크는 친구만 볼 수 있습니다.";
            default -> "권한이 없습니다.";
        };
    }

    public ResponseDto<CandleListDto> deleteCandle(CandleDeleteRequestDto requestDto) {
        Candle candle = candleRepository.findById(requestDto.getCandleId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CANDLE_NOT_FOUND));

        candleRepository.delete(candle);
        return new ResponseDto<>("캔들 삭제", null);
    }
}

