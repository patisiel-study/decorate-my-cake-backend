package com.example.decoratemycakebackend.domain.candle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleGetByCakeRequestDto {
    private String email;
    private Integer cakeCreatedYear;

}
