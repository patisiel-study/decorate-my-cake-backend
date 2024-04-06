package com.example.decoratemycakebackend.domain.friend.repository;

import com.example.decoratemycakebackend.domain.friend.entity.Friendship;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    void deleteByMember1AndMember2(Member member1, Member member2);
    boolean existsByMember1AndMember2(Member member1, Member member2);
}
