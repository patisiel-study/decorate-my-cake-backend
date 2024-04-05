package com.example.decoratemycakebackend.domain.friend.repository;

import com.example.decoratemycakebackend.domain.friend.entity.Friendship;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByMember1OrMember2(Member member1, Member member2);
}
