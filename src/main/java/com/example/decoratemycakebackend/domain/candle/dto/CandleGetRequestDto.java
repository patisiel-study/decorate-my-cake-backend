package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleGetRequestDto {
    private String email;
    private int cakeCreatedYear;
}
