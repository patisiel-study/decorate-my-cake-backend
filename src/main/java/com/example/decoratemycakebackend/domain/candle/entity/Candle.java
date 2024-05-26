package com.example.decoratemycakebackend.domain.candle.entity;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Candle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candle_id", nullable = false)
    private Long candleId;
    private String CandleName; // 캔들과 캔들 이미지 이름 동일함.
    private String CandleContent; // 캔들 메시지의 내용
    private String CandleTitle; // 캔들 메시지의 제목
    private boolean isPrivate; // 비밀글이면 true, 공개글이면 false
    private String writer; // 기본적으로 작성자의 닉네임, 프론트 측에서 변경 가능함.
    @CreatedDate
    private LocalDateTime candleCreatedAt;
    private long totalCandleCount;
    private String message;

    public boolean isPrivate() {
        return isPrivate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cake_id")
    private Cake cake;

}