package com.example.decoratemycakebackend.domain.cake.dto;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCountPermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleCreatePermission;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class CakeViewResponseDto {
    private String message;
    private String nickname;
    private String cakeName;
    private String birthday;
    private Integer candleCount;
    private Integer cakeCreatedYear;
    private CandleCreatePermission candleCreatePermission;
    private CandleViewPermission candleViewPermission;
    private CandleCountPermission candleCountPermission;

    public static CakeViewResponseDto toDto(Cake cake, Member member, String message) {

        return CakeViewResponseDto.builder()
                .message(message)
                .nickname(member.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(member.getBirthday().toString())
                .candleCount(cake.getCandles().size())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleCreatePermission(cake.getCandleCreatePermission())
                .candleViewPermission(cake.getCandleViewPermission())
                .candleCountPermission(cake.getCandleCountPermission())
                .build();
    }

    public static CakeViewResponseDto toDtoForFriend(Cake cake, Member member, String message) {
        int candleCount = cake.getCandleCountPermission() == CandleCountPermission.ANYONE ? cake.getCandles().size() : -1;

        return CakeViewResponseDto.builder()
                .message(message)
                .nickname(member.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(member.getBirthday().toString())
                .candleCount(candleCount)
                .cakeCreatedYear(cake.getCreatedYear())
                .candleCreatePermission(cake.getCandleCreatePermission())
                .candleViewPermission(cake.getCandleViewPermission())
                .candleCountPermission(cake.getCandleCountPermission())
                .build();
    }
}