package com.example.decoratemycakebackend.domain.member.dto;

import com.example.decoratemycakebackend.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private String profileImg;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .profileImg(member.getProfileImg())
                .build();
    }
}
