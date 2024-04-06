package com.example.decoratemycakebackend.domain.friend.service;

import com.example.decoratemycakebackend.domain.friend.dto.*;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequest;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequestStatus;
import com.example.decoratemycakebackend.domain.friend.repository.FriendRequestRepository;
import com.example.decoratemycakebackend.domain.friend.repository.FriendshipRepository;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import com.example.decoratemycakebackend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;

    public void sendFriendRequest(FriendRequestDto friendRequestDto) {
        Member sender = memberRepository.findByEmail(friendRequestDto.getSenderEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findByEmail(friendRequestDto.getReceiverEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 기존 요청 확인
        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        if (existingRequest.isPresent()) {
            FriendRequest friendRequest = existingRequest.get();
            switch (friendRequest.getStatus()) {
                case REJECTED, DELETED:
                    // 거절된 요청이었던 상태를 승인 대기중 상태로 변경
                    FriendRequest updatedRequest = friendRequest.updateToPending(friendRequestDto.getMessage());
                    friendRequestRepository.save(updatedRequest);
                    break;
                case PENDING:
                    // 이미 승인 대기중인 요청이 있는 경우
                    throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
                case ACCEPTED:
                    // 이미 친구 관계인 경우
                    throw new CustomException(ErrorCode.ALREADY_FRIEND);
            }
        } else {
            // 새로운 친구 요청 생성
            FriendRequest friendRequest = FriendRequest.builder()
                    .receiver(receiver)
                    .sender(sender)
                    .status(FriendRequestStatus.PENDING)
                    .message(friendRequestDto.getMessage())
                    .build();

            friendRequestRepository.save(friendRequest);
        }
    }

    public String confirmFriendRequest(FriendRequestAnswerDto friendRequestAnswerDto) {
        Member sender = memberRepository.findByEmail(friendRequestAnswerDto.getSenderEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findByEmail(friendRequestAnswerDto.getReceiverEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (friendRequestAnswerDto.isAccepted()) {
            FriendRequest acceptedRequest = friendRequest.acceptRequest();
            friendRequestRepository.save(acceptedRequest);

            // 친구 관계 생성
//            Friendship friendship = Friendship.builder()
//                    .member1(sender)
//                    .member2(receiver)
//                    .build();
//            friendshipRepository.save(friendship);

            return "친구 요청이 수락되었습니다!";
        } else {
            FriendRequest rejectedRequest = friendRequest.rejectRequest();
            friendRequestRepository.save(rejectedRequest);

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

    @Transactional
    public void deleteFriend(FriendDeleteRequestDto friendDeleteRequestDto) {
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("Current member: {}", currentMember.getEmail());

        Member friendMember = memberRepository.findByEmail(friendDeleteRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("Friend member: {}", friendMember.getEmail());

        // 두 멤버가 친구 상태인지 조회
        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(currentMember, friendMember, FriendRequestStatus.ACCEPTED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FRIEND));

        // 친구 요청 상태를 DELETED로 변경
        FriendRequest deletedRequest = friendRequest.deleteRequest();
        friendRequestRepository.save(deletedRequest);
        log.info("Friendship deleted between {} and {}", currentMember.getEmail(), friendMember.getEmail());
    }
}
