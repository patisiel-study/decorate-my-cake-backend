package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleGetRequestDto {
    private String email;
    private int cakecreatedYear;
}
