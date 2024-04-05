package com.example.decoratemycakebackend.domain.friend.repository;

import com.example.decoratemycakebackend.domain.friend.entity.FriendRequest;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequestStatus;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.receiver = :member OR fr.sender = :member) AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedFriendRequestsByMember(@Param("member") Member member);

    List<FriendRequest> findByReceiverAndStatus(Member receiver, FriendRequestStatus status);
}
