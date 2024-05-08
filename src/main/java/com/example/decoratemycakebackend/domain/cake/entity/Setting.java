/*package com.example.decoratemycakebackend.domain.cake.entity;

import com.example.decoratemycakebackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class Setting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cakeSetting_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CandleMakePermission candleMakePermission;

    @Enumerated(EnumType.STRING)
    private CandleViewPermission candleViewPermission;

    private boolean candleCountPermission;

    @OneToOne
    @JoinColumn(name = "cake_id")
    private Cake cake;

    public boolean isCandleCountPermission() {
        return candleCountPermission;
    }

    public enum CandleMakePermission {
        ANYONE, ONLY_ME
    }

    public enum CandleViewPermission {
        ANYONE, ONLY_ME
    }
}*/