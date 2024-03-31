package com.example.decoratemycakebackend.domain.cake.dto;

import com.example.decoratemycakebackend.domain.cake.entity.Setting;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SettingDto {
    @Enumerated(EnumType.STRING)
    private Setting.CandleMakePermission candleMakePermission;

    @Enumerated(EnumType.STRING)
    private Setting.CandleViewPermission candleViewPermission;

    private boolean candleCountPermission;
}