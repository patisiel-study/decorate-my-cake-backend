package com.example.decoratemycakebackend.domain.friend.entity;

import com.example.decoratemycakebackend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id")
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_id")
    private Member member2;
}