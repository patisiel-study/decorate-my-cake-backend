package com.example.decoratemycakebackend.domain.cake.entity;

import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class Cake extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cake_id", nullable = false)
    private Long id;
    private String nickname;
    private String cakeName;
    private LocalDateTime createCreatedAt;
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "cake", cascade = CascadeType.ALL)
    private Setting setting;

    @OneToMany(mappedBy = "cake", cascade = CascadeType.ALL)
    private List<Candle> candles = new ArrayList<>();

    public Cake(String cakeName, LocalDateTime createCreatedAt, LocalDateTime updateAt) {
        this.cakeName = cakeName;
        this.createCreatedAt = createCreatedAt;
        this.updateAt = updateAt;
    }

}