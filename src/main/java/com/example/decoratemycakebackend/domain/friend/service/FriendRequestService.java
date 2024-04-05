package com.example.decoratemycakebackend.domain.friend.service;

import com.example.decoratemycakebackend.domain.friend.dto.*;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequest;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequestStatus;
import com.example.decoratemycakebackend.domain.friend.entity.Friendship;
import com.example.decoratemycakebackend.domain.friend.repository.FriendRequestRepository;
import com.example.decoratemycakebackend.domain.friend.repository.FriendshipRepository;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;

    public void sendFriendRequest(FriendRequestDto friendRequestDto) {
        Member sender = memberRepository.findByEmail(friendRequestDto.getSenderEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findByEmail(friendRequestDto.getReceiverEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        FriendRequest friendRequest = FriendRequest.builder()
                .receiver(receiver)
                .sender(sender)
                .status(FriendRequestStatus.PENDING)
                .message(friendRequestDto.getMessage())
                .build();

        friendRequestRepository.save(friendRequest);
    }

    public String confirmFriendRequest(FriendRequestAnswerDto friendRequestAnswerDto) {
        Member sender = memberRepository.findByEmail(friendRequestAnswerDto.getSenderEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findByEmail(friendRequestAnswerDto.getReceiverEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (friendRequestAnswerDto.isAccepted()) {
            friendRequest.accept();
            friendRequestRepository.save(friendRequest);

            // 친구 관계 생성
            Friendship friendship = Friendship.builder()
                    .member1(sender)
                    .member2(receiver)
                    .build();
            friendshipRepository.save(friendship);

            return "친구 요청이 수락되었습니다!";
        } else {
            friendRequest.reject();
            friendRequestRepository.save(friendRequest);

            return "친구 요청이 거부되었습니다.";
        }
    }

    public List<FriendListResponseDto> getFriendList(FriendListRequestDto friendListRequestDto) {
        Member member = memberRepository.findByEmail(friendListRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FriendRequest> acceptedFriendRequests = friendRequestRepository.findAcceptedFriendRequestsByMember(member);

        return acceptedFriendRequests.stream()
                .map(friendRequest -> {
                    Member friend = friendRequest.getReceiver().equals(member) ? friendRequest.getSender() : friendRequest.getReceiver();
                    return new FriendListResponseDto(
                            friend.getId(),
                            friend.getNickname(),
                            friend.getEmail(),
                            friend.getBirthday(),
                            friend.getProfileImg()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestListResponseDto> getFriendRequestList(FriendRequestListRequestDto friendRequestListRequestDto) {
        Member member = memberRepository.findByEmail(friendRequestListRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(member, FriendRequestStatus.PENDING);

        return friendRequests.stream()
                .map(friendRequest -> {
                    Member sender = friendRequest.getSender();
                    return new FriendRequestListResponseDto(
                            sender.getId(),
                            sender.getNickname(),
                            sender.getEmail(),
                            sender.getBirthday(),
                            sender.getProfileImg()
                    );
                })
                .collect(Collectors.toList());
    }
}
