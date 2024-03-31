package com.example.decoratemycakebackend.domain.member.dto;

import com.example.decoratemycakebackend.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCakeDto {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthday;


}

