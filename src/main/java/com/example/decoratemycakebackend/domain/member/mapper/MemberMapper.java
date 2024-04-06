package com.example.decoratemycakebackend.domain.member.mapper;

import com.example.decoratemycakebackend.domain.friend.dto.FriendListResponseDto;
import com.example.decoratemycakebackend.domain.friend.dto.FriendRequestListResponseDto;
import com.example.decoratemycakebackend.domain.member.dto.MemberDto;
import com.example.decoratemycakebackend.domain.member.dto.SignUpDto;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDto toMemberDto(Member member);
    FriendListResponseDto toFriendListResponseDto(Member member);
    FriendRequestListResponseDto toFriendRequestListResponseDto(Member member);
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "roles", source = "roles")
    Member toMember(SignUpDto signUpDto, String encodedPassword, List<String> roles);
}
