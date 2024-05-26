package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleAddRequestDto {
    //private long candleId;
    private String candleTitle;
    private String candleContent;
    private String candleName;
    private boolean isPrivate;
    private String writer;
    private String email;
    private int cakecreatedYear;

}
