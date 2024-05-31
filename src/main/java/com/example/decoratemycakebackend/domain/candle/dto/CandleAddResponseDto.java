package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleAddResponseDto {
    private long candleId;
    private String candleName;
    private String candleTitle;
    private String candleContent;
    private LocalDate candleCreatedAt;
    private String writer;
    private boolean isPrivate;
}

