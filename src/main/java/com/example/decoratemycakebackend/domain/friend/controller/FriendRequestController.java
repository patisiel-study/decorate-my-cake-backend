package com.example.decoratemycakebackend.domain.friend.controller;

import com.example.decoratemycakebackend.domain.friend.dto.FriendListResponseDto;
import com.example.decoratemycakebackend.domain.friend.dto.FriendRequestAnswerDto;
import com.example.decoratemycakebackend.domain.friend.dto.FriendRequestDto;
import com.example.decoratemycakebackend.domain.friend.service.FriendRequestService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "친구 관리 API", description = "친구 관리 API endpoints")
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @Operation(summary = "친구 요청", description = "메시지는 필수 아님. 요청 중복 불가. receiverEmail: 친구 요청을 받는 유저의 이메일")
    @PostMapping("/request")
    public ResponseEntity<ResponseDto<?>> createFriendRequest(@Valid @RequestBody FriendRequestDto friendRequestDto) {
        friendRequestService.sendFriendRequest(friendRequestDto);
        return ResponseEntity.ok(new ResponseDto<>("친구 요청이 완료되었습니다.", null));
    }

    @Operation(summary = "친구 요청 수락/거절", description = "receiverEmail: 친구 요청을 보냈던 유저의 이메일")
    @PostMapping("/request/confirm")
    public ResponseEntity<ResponseDto<?>> confirmFriendRequest(@Valid @RequestBody FriendRequestAnswerDto friendRequestAnswerDto) {
        String message = friendRequestService.confirmFriendRequest(friendRequestAnswerDto);
        return ResponseEntity.ok(new ResponseDto<>(message, null));
    }

    @Operation(summary = "친구 목록 조회", description = "해당 유저의 친구 목록 조회. 메시지는 필수 아님")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<?>> getFriendList() {
        List<FriendListResponseDto> friendList = friendRequestService.getFriendList();
        return ResponseEntity.ok(new ResponseDto<>("친구 목록 조회가 완료되었습니다.", friendList));
    }

    @Operation(summary = "친구 요청 목록 조회", description = "해당 유저가 받은 친구 요청들에 대한 목록 조회")
    @GetMapping("/request/list")
    public ResponseEntity<ResponseDto<?>> getFriendRequestList() {
        return ResponseEntity.ok(new ResponseDto<>("친구 요청 목록 조회가 완료되었습니다.", friendRequestService.getFriendRequestList()));
    }

    @Operation(summary = "친구 삭제", description = "해당 유저를 친구 목록에서 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<?>> deleteFriend(@RequestParam("friendEmail") @Email(message = "올바른 이메일 형식이 필요합니다.") String friendEmail) {
        friendRequestService.deleteFriend(friendEmail);
        return ResponseEntity.ok(new ResponseDto<>("친구 삭제가 완료되었습니다.", null));
    }

}
