package com.example.decoratemycakebackend.domain.friend.controller;

import com.example.decoratemycakebackend.domain.friend.dto.*;
import com.example.decoratemycakebackend.domain.friend.service.FriendRequestService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/request")
    public ResponseEntity<ResponseDto<?>> createFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendRequestService.sendFriendRequest(friendRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("친구 요청이 완료되었습니다.", null));
    }

    @PostMapping("/request/confirm")
    public ResponseEntity<ResponseDto<?>> confirmFriendRequest(@RequestBody FriendRequestAnswerDto friendRequestAnswerDto) {
        String message = friendRequestService.confirmFriendRequest(friendRequestAnswerDto);
        return ResponseEntity.ok(new ResponseDto<>(message, null));
    }

    @PostMapping("/list")
    public ResponseEntity<ResponseDto<?>> getFriendList(@RequestBody FriendListRequestDto friendListRequestDto) {
        List<FriendListResponseDto> friendList = friendRequestService.getFriendList(friendListRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("친구 목록 조회가 완료되었습니다.", friendList));
    }

    @PostMapping("/request/list")
    public ResponseEntity<ResponseDto<?>> getFriendRequestList(@RequestBody FriendRequestListRequestDto friendRequestListRequestDto) {
        return ResponseEntity.ok(new ResponseDto<>("친구 요청 목록 조회가 완료되었습니다.", friendRequestService.getFriendRequestList(friendRequestListRequestDto)));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseDto<?>> deleteFriend(@RequestBody FriendDeleteRequestDto friendDeleteRequestDto) {
        friendRequestService.deleteFriend(friendDeleteRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("친구 삭제가 완료되었습니다.", null));
    }

}
