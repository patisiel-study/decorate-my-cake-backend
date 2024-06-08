package com.example.decoratemycakebackend.domain.cake.repository;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CakeRepository extends JpaRepository<Cake, Long> {

    List<Cake> findAllByMemberEmail(String email);

    Optional<Cake> findByEmailAndCreatedYear(String email, int year);

    // 최신 연도의 케이크를 가져오는 쿼리 메서드
    @Query("SELECT c FROM Cake c WHERE c.email = :email ORDER BY c.createdYear DESC")
    Optional<Cake> findLatestCakeByEmail(String email);
}
