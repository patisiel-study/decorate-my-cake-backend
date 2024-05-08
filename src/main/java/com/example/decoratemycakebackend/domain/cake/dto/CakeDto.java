package com.example.decoratemycakebackend.domain.cake.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CakeDto {
    private Long id;
    private String cakeName;
    private LocalDate cakeCreatedAt;
    private LocalDate updateAt;
}