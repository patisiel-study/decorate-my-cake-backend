package com.example.decoratemycakebackend.domain.candle.dto;

import com.example.decoratemycakebackend.domain.candle.entity.CandleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleAddRequestDto {
    private String candleTitle;
    private String candleContent;
    private CandleName candleName;
    private boolean isPrivate;
    private String writer;
    private String cakeOwnerEmail;
    private Integer cakeCreatedYear;
}
