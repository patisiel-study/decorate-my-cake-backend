package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleListDto {
    private Long candleId;
    private String candleName;
    private String candleTitle;
    private String candleContent;
    private String candleCreatedAt;
    private String writer;
    private boolean isPrivate;


}
