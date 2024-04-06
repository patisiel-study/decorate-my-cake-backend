package com.example.decoratemycakebackend.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FriendRequestListResponseDto {
    private Long memberId;
    private String nickname;
    private String email;
    private LocalDate birthday;
    private String profileImg;
}
