package com.example.decoratemycakebackend.domain.candle.service;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCountPermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCreatePermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleService {

    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;
    private final CandleRepository candleRepository;
    private final FriendRequestRepository friendRequestRepository;


    public ResponseDto<CandleListDto> addCandle(CandleAddRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakecreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        Candle candle = Candle.builder()
                //.candleId(requestDto.getCandleId())
                .title(requestDto.getCandleTitle())
                .content(requestDto.getCandleContent())
                //.name(requestDto.getCandleName())
                .writer(requestDto.getWriter())
                .isPrivate(requestDto.isPrivate())
                .cake(cake) // cake 필드 설정
                .candleCreatedAt(LocalDateTime.now())
                .build();

        candleRepository.save(candle);
        cakeRepository.save(cake);

        String candleCreatedAt = candle.getCreatedAt() != null ? candle.getCreatedAt().toString() : null;

        CandleListDto candleListDto = CandleListDto.builder()
                .candleId(candle.getCandleId())
                //.candleName(candle.getName())
                .candleTitle(candle.getTitle())
                .candleContent(candle.getContent())
                .candleCreatedAt(candle.getCandleCreatedAt())
                .writer(candle.getWriter())
                .isPrivate(candle.isPrivate())
                .build();

        return new ResponseDto<>("캔들 작성이 완료되었습니다", candleListDto);
    }

    public ResponseDto<CandleListDto> addCandleFriends(CandleAddFriendsRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakecreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        // 케이크의 CandleCreatePermission이 ONLY_FRIENDS인 경우
        if (cake.getCandleCreatePermission() == CandleCreatePermission.ONLY_FRIENDS) {
            // 현재 사용자와 케이크 소유자가 친구 관계인지 확인
            FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FRIEND));

            // 친구 관계라면 캔들 작성 허용
            Candle candle = Candle.builder()
                    .title(requestDto.getCandleTitle())
                    .content(requestDto.getCandleContent())
                    //.name(requestDto.getCandleName())
                    .writer(requestDto.getWriter())
                    .isPrivate(requestDto.isPrivate())
                    .cake(cake)
                    .candleCreatedAt(LocalDateTime.now())
                    .build();

            Candle savedCandle = candleRepository.save(candle);
            Cake savedCake = cakeRepository.save(cake);

            CandleListDto candleListDto = CandleListDto.builder()
                    .candleId(savedCandle.getCandleId())
                    //.candleName(savedCandle.getName())
                    .candleTitle(savedCandle.getTitle())
                    .candleContent(savedCandle.getContent())
                    .candleCreatedAt(savedCandle.getCandleCreatedAt())
                    .writer(savedCandle.getWriter())
                    .isPrivate(savedCandle.isPrivate())
                    .build();

            return new ResponseDto<>("캔들 작성이 완료되었습니다.", candleListDto);
        }

        // CandleCreatePermission이 ONLY_FRIENDS가 아니거나 친구 관계가 아닌 경우
        return new ResponseDto<>("캔들 작성 권한이 없습니다.", null);
    }

    public Page<CandleListDto> getCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        long totalCandleCount = candleRepository.totalcandlecount();

        return candlePage.map(candle -> {
            boolean view = false;

            if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) {
                view = true;
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_ME) {
                view = currentMember.equals(cake.getMember());
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) {
                FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED)
                        .orElse(null);
                view = (friendRequest != null);
            }

            if (cake.getCandleCountPermission() == CandleCountPermission.ONLY_ME && !currentMember.equals(cake.getMember())) {
                view = false;
            }

            if (view) {
                return CandleListDto.builder()
                        .candleId(candle.getCandleId())
                        //.candleName(candle.getName())
                        .candleTitle(candle.getTitle())
                        .candleContent(candle.getContent())
                        .candleCreatedAt(candle.getCandleCreatedAt())
                        .totalcandlecount(candlePage.getTotalElements())
                        .writer(candle.getWriter())
                        .isPrivate(candle.isPrivate())
                        .build();
            } else {
                return new CandleListDto();
            }
        });
    }

    public ResponseDto<List<CandleListDto>> getDescCandle(CandleGetRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        //List<Candle> candles = cake.getCandles();
        //List<Candle> candles = candleRepository.findBySort(cake);
        List<Candle> candles = candleRepository.findByCakeOrderByCandleCreatedAtDesc(cake);
        long totalCandleCount = candleRepository.totalcandlecount();

        List<CandleListDto> responseDtos = new ArrayList<>();

        //candles.sort(Comparator.comparing(Candle::getCandlecreatedAt).reversed());

        for (Candle candle : candles) {
            boolean view = false;

            if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) {
                view = true;
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_ME) {
                view = currentMember.equals(cake.getMember());
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) {
                FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED)
                        .orElse(null);
                view = (friendRequest != null);
            }

            if (cake.getCandleCountPermission() == CandleCountPermission.ONLY_ME && !currentMember.equals(cake.getMember())) {
                view = false;
            }

            if (view) {
                CandleListDto candleListDto = CandleListDto.builder()
                        .candleId(candle.getCandleId())
                        //.candleName(candle.getName())
                        .candleTitle(candle.getTitle())
                        .candleContent(candle.getContent())
                        .candleCreatedAt(candle.getCandleCreatedAt())
                        .writer(candle.getWriter())
                        .totalcandlecount(totalCandleCount)
                        .isPrivate(candle.isPrivate())
                        .build();

                responseDtos.add(candleListDto);
            }
        }

        return new ResponseDto<>("캔들 목록 조회 성공", responseDtos);
    }

    public ResponseDto<List<CandleListDto>> getYearCandle(CandleGetRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakeCreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        long totalCandleCount = candleRepository.totalcandlecount();
        List<Candle> candles = candleRepository.findByCakeOrderByYearAndCandleCreatedAtDesc(cake);

        List<CandleListDto> responseDtos = new ArrayList<>();


        for (Candle candle : candles) {
            boolean view = false;

            if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) {
                view = true;
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_ME) {
                view = currentMember.equals(cake.getMember());
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) {
                FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, cake.getMember(), FriendRequestStatus.ACCEPTED)
                        .orElse(null);
                view = (friendRequest != null);
            }

            if (cake.getCandleCountPermission() == CandleCountPermission.ONLY_ME && !currentMember.equals(cake.getMember())) {
                view = false;
            }

            if (view) {
                CandleListDto candleListDto = CandleListDto.builder()
                        .candleId(candle.getCandleId())
                        //.candleName(candle.getName())
                        .candleTitle(candle.getTitle())
                        .candleContent(candle.getContent())
                        .candleCreatedAt(candle.getCandleCreatedAt())
                        .writer(candle.getWriter())
                        .totalcandlecount(totalCandleCount)
                        .isPrivate(candle.isPrivate())
                        .build();

                responseDtos.add(candleListDto);
            }
        }

        return new ResponseDto<>("캔들 목록 조회 성공", responseDtos);
    }

    public ResponseDto<CandleListDto> deleteCandle(CandleDeleteRequestDto requestDto) {
        Candle candle = candleRepository.findById(requestDto.getCandleId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CANDLE_NOT_FOUND));


        candleRepository.delete(candle);
        return new ResponseDto<>("캔들 삭제", null);
    }
}

