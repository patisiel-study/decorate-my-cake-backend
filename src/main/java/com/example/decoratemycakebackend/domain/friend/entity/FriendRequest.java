package com.example.decoratemycakebackend.domain.friend.entity;

import com.example.decoratemycakebackend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    private String message;

    private String profileImg;

//    public void accept() {
//        this.status = FriendRequestStatus.ACCEPTED;
//    }
//
//    public void reject() {
//        this.status = FriendRequestStatus.REJECTED;
//    }

    public FriendRequest updateToPending(String message) {
        if (this.status != FriendRequestStatus.REJECTED && this.status != FriendRequestStatus.DELETED) {
            throw new IllegalStateException("Only rejected friend requests can be updated to pending.");
        }
        return FriendRequest.builder()
                .id(this.id)
                .sender(this.sender)
                .receiver(this.receiver)
                .status(FriendRequestStatus.PENDING)
                .message(message)
                .profileImg(this.profileImg)
                .build();
    }

    public FriendRequest acceptRequest() {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending friend requests can be accepted.");
        }
        return FriendRequest.builder()
                .id(this.id)
                .sender(this.sender)
                .receiver(this.receiver)
                .status(FriendRequestStatus.ACCEPTED)
                .message(this.message)
                .profileImg(this.profileImg)
                .build();
    }

    public FriendRequest rejectRequest() {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending friend requests can be rejected.");
        }
        return FriendRequest.builder()
                .id(this.id)
                .sender(this.sender)
                .receiver(this.receiver)
                .status(FriendRequestStatus.REJECTED)
                .message(this.message)
                .profileImg(this.profileImg)
                .build();
    }

    public FriendRequest deleteRequest() {
        if (this.status != FriendRequestStatus.ACCEPTED) {
            throw new IllegalStateException("Only accepted friend requests can be deleted.");
        }
        return FriendRequest.builder()
                .id(this.id)
                .sender(this.sender)
                .receiver(this.receiver)
                .status(FriendRequestStatus.DELETED)
                .message(this.message)
                .profileImg(this.profileImg)
                .build();
    }

}
