package com.example.decoratemycakebackend.domain.cake.dto;

import com.example.decoratemycakebackend.domain.cake.entity.CandleCountPermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCreatePermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CakeCreateResponseDto {
    private String message;
    private String nickname;
    private String cakeName;
    private String birthday;
    private Integer cakeCreatedYear;
    private CandleCreatePermission candleCreatePermission;
    private CandleViewPermission candleViewPermission;
    private CandleCountPermission candleCountPermission;

}