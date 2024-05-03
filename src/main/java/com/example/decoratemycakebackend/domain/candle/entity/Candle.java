package com.example.decoratemycakebackend.domain.candle.entity;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Candle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String candleContent;
    private String candleName;
    private LocalDate createdAt;
    private String candleTitle;
    private Boolean Private;
    private String writer;

    @ManyToOne
    @JoinColumn(name="cake_id")
    private Cake cake;
}