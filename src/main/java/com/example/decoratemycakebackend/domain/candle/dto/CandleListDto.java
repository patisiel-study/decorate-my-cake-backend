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
    private long totalcandlecount;


}
