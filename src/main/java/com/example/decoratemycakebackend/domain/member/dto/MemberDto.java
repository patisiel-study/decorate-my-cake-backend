package com.example.decoratemycakebackend.domain.member.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long memberId;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private String profileImg;
}
