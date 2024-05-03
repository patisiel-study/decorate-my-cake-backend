package com.example.decoratemycakebackend.domain.cake.dto;

import com.example.decoratemycakebackend.domain.candle.dto.CandleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CakeGetResponseDto {
    private String nickname;
    private String email;
    private String cakeName;
    private LocalDate birthday;
    private LocalDate CreatedAt;
    private List<CandleDto> candleList;
    private int totalCandle;
    //private int totalPage;
    //private int offset;
    private SettingDto setting;
    //private CakePageInfoDto pageInfo;
}