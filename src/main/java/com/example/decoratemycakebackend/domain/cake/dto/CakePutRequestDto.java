package com.example.decoratemycakebackend.domain.cake.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CakePutRequestDto {
    private String email;
    //private LocalDate createdAt;
    private String cakeName;
    private SettingDto setting;


}