package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {
    private Long id;
    private String candleName;
    private LocalDateTime createAt;
    private String candleTitle;
    private Boolean Private;
    private String writer;


}
