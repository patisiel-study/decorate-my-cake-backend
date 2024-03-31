package com.example.decoratemycakebackend.domain.cake.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CakeDto {
    private Long id;
    private String cakeName;
    private LocalDateTime cakeCreatedAt;
    private LocalDateTime updateAt;
}