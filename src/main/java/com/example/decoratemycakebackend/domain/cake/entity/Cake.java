package com.example.decoratemycakebackend.domain.cake.entity;

import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    //private String nickname;
    //private String cakeName;
    //private LocalDate createdAt;
    private int createdYear;
    private String email;
    private String cakeName;

    //@ManyToOne
    //@JoinColumn(name = "member_id")
    //private Member member;

    //@OneToOne(mappedBy = "cake", cascade = CascadeType.ALL)
    //private Setting setting = new Setting();

    //@OneToMany(mappedBy = "cake", cascade = CascadeType.ALL)
    //private List<Candle> candles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CandleCreatePermission candleCreatePermission;

    @Enumerated(EnumType.STRING)
    private CandleViewPermission candleViewPermission;

    @Enumerated(EnumType.STRING)
    private CandleCountPermission candleCountPermission;

    @OneToMany(mappedBy = "cake", cascade = CascadeType.ALL)
    private List<Candle> candles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public int getCakeCreatedYear() {
        return getCreatedAt().getYear();
    }

    public void updatePermissions(CandleCreatePermission candleCreatePermission,
                                  CandleViewPermission candleViewPermission,
                                  CandleCountPermission candleCountPermission) {
        this.candleCreatePermission = candleCreatePermission;
        this.candleViewPermission = candleViewPermission;
        this.candleCountPermission = candleCountPermission;
    }

}