package com.example.decoratemycakebackend.domain.cake.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CakeAddRequestDto {
    private String email;
    private LocalDate createdAt;
    private String cakeName;
    private SettingDto setting;


}

