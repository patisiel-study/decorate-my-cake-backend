package com.example.decoratemycakebackend.domain.candle.service;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCountPermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCreatePermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.CandleAddRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleDeleteRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleGetRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleListDto;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.candle.repsository.CandleRepository;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequest;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequestStatus;
import com.example.decoratemycakebackend.domain.friend.repository.FriendRequestRepository;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import com.example.decoratemycakebackend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleService {

    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;
    private final CandleRepository candleRepository;
    private final FriendRequestRepository friendRequestRepository;

    public CandleListDto addCandle(CandleAddRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getCakeowneremail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        // 케이크의 CandleCreatePermission 확인
        if (cake.getCandleCreatePermission() == CandleCreatePermission.ONLY_FRIENDS) {
            // 현재 사용자와 케이크 소유자가 친구 관계인지 확인
            FriendRequest xfriendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FRIEND));
        }

        // 캔들 생성 로직 진행
        Candle candle = Candle.builder()
                .CandleTitle(requestDto.getCandleTitle())
                .CandleContent(requestDto.getCandleContent())
                .writer(requestDto.getWriter())
                .isPrivate(requestDto.isPrivate())
                .cake(cake)
                .candleCreatedAt(LocalDateTime.now())
                .build();

        Candle savedCandle = candleRepository.save(candle);
        Cake savedCake = cakeRepository.save(cake);

        // CandleListDto 생성 및 반환
        return CandleListDto.builder()
                .candleId(savedCandle.getCandleId())
                .candleTitle(savedCandle.getCandleTitle())
                .candleContent(savedCandle.getCandleContent())
                .candleCreatedAt(savedCandle.getCandleCreatedAt())
                .writer(savedCandle.getWriter())
                .isPrivate(savedCandle.isPrivate())
                .build();
    }

    public Page<CandleListDto> getCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        long totalCandleCount = candleRepository.countByCake(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(candle.getCandleTitle())
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? totalCandleCount : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle && candle.isPrivate())
                .message(message)
                .build());
    }


    public Page<CandleListDto> getDescCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCakeOrderByCandleCreatedAtDesc(cake, pageable);
        long totalCandleCount = candleRepository.countByCake(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? totalCandleCount : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle && candle.isPrivate())
                .message(message)
                .build());
    }

    public Page<CandleListDto> getYearAscCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCakeOrderByYearAndCandleCreatedAtAsc(cake, pageable);
        long totalCandleCount = candleRepository.countByCake(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? totalCandleCount : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle && candle.isPrivate())
                .message(message)
                .build());
    }
    //오래된순으로만
    public Page<CandleListDto> getAscCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCakeOrderByCandleCreatedAtAsc(cake, pageable);
        long totalCandleCount = candleRepository.countByCake(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? totalCandleCount : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle && candle.isPrivate())
                .message(message)
                .build());
    }

    public Page<CandleListDto> getYearDescCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCakeOrderByYearAndCandleCreatedAtDesc(cake, pageable);
        long totalCandleCount = candleRepository.countByCake(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? totalCandleCount : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle && candle.isPrivate())
                .message(message)
                .build());
    }


    private Member getCurrentMember() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Cake getCake(CandleGetRequestDto requestDto) {
        return cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));
    }

    private boolean canViewCandle(Cake cake, Member currentMember) {
        if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) {
            return true;
        } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_ME) {
            return currentMember.equals(cake.getMember());
        } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) {
            Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED);
            return friendRequestOptional.isPresent();
        }
        return false;
    }

    private boolean canViewCandleCount(Cake cake, Member currentMember) {
        return cake.getCandleCountPermission() != CandleCountPermission.ONLY_ME || currentMember.equals(cake.getMember());
    }

    private String getViewCandleMessage(Cake cake, boolean canViewCandle) {
        if (canViewCandle) {
            return null;
        } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_ME) {
            return "이 케이크는 케이크 주인만 볼 수 있습니다.";
        } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) {
            return "이 케이크는 친구만 볼 수 있습니다.";
        } else {
            return "권한이 없습니다.";
        }
    }


    public ResponseDto<CandleListDto> deleteCandle(CandleDeleteRequestDto requestDto) {
        Candle candle = candleRepository.findById(requestDto.getCandleId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CANDLE_NOT_FOUND));


        candleRepository.delete(candle);
        return new ResponseDto<>("캔들 삭제", null);
    }
}

