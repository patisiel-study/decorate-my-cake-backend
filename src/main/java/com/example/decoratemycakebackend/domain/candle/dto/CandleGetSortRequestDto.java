package com.example.decoratemycakebackend.domain.candle.dto;

import java.time.LocalDate;

public class CandleGetSortRequestDto {
    private long candleId;
    private String candleName;
    private String candleTitle;
    private String candleContent;
    private LocalDate candlecreatedAt;
    private String writer;
    private boolean isPrivate;
}
