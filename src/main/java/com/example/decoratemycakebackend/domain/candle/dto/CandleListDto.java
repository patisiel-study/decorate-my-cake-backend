package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleListDto {
    private Long candleId;
    //private String candleName;
    private String candleTitle;
    private String candleContent;
    private LocalDateTime candleCreatedAt;
    private String writer;
    private boolean isPrivate;
    private long totalCandleCount;
    private String message;
}

/*public Page<CandleListDto> getDescCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        List<Candle> candles = candleRepository.findByCakeOrderByCandleCreatedAtDesc(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? candlePage.getTotalElements() : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle ? candle.isPrivate() : false) // null 대신 기본값 false 사용
                .message(message)
                .build());
    }


    public Page<CandleListDto> getAscCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        List<Candle> candles = candleRepository.findByCakeOrderByCandleCreatedAtAsc(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? candlePage.getTotalElements() : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle ? candle.isPrivate() : false) // null 대신 기본값 false 사용
                .message(message)
                .build());
    }

    public Page<CandleListDto> getYearAscCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        List<Candle> candles = candleRepository.findByCakeOrderByYearAndCandleCreatedAtAsc(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? candlePage.getTotalElements() : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle ? candle.isPrivate() : false) // null 대신 기본값 false 사용
                .message(message)
                .build());
    }

    //오래된순으로만

    public Page<CandleListDto> getYearDescCandle(CandleGetRequestDto requestDto, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Cake cake = getCake(requestDto);

        boolean canViewCandle = canViewCandle(cake, currentMember);
        boolean canViewCandleCount = canViewCandleCount(cake, currentMember);
        String message = getViewCandleMessage(cake, canViewCandle);

        Page<Candle> candlePage = candleRepository.findByCake(cake, pageable);
        List<Candle> candles = candleRepository.findByCakeOrderByYearAndCandleCreatedAtDesc(cake);

        return candlePage.map(candle -> CandleListDto.builder()
                .candleId(candle.getCandleId())
                .candleTitle(canViewCandle ? candle.getCandleTitle() : null)
                .candleContent(canViewCandle ? candle.getCandleContent() : null)
                .candleCreatedAt(candle.getCandleCreatedAt())
                .totalCandleCount(canViewCandleCount ? candlePage.getTotalElements() : 0)
                .writer(canViewCandle ? candle.getWriter() : null)
                .isPrivate(canViewCandle ? candle.isPrivate() : false) // null 대신 기본값 false 사용
                .message(message)
                .build());
    }

    public ResponseDto<CandleListDto> deleteCandle(CandleDeleteRequestDto requestDto) {
        Candle candle = candleRepository.findById(requestDto.getCandleId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CANDLE_NOT_FOUND));


        candleRepository.delete(candle);
        return new ResponseDto<>("캔들 삭제", null);
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
    }*/