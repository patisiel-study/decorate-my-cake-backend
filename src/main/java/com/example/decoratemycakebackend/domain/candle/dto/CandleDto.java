package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {
    private Long id;
    private String candleName;
    private LocalDate createdAt;
    private String candleTitle;
    private String candleContent;
    private Boolean Private;
    private String writer;


}
