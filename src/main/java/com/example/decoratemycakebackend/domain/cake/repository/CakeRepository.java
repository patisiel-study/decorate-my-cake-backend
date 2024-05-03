package com.example.decoratemycakebackend.domain.cake.repository;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CakeRepository extends JpaRepository<Cake, Long> {
    @Query("SELECT c FROM Cake c WHERE c.member.email = :email AND c.createdAt = :createdAt")
    Optional<Cake> findByMemberEmailAndCreatedAt(@Param("email") String email, @Param("createdAt") LocalDate createdAt);

    @Query("SELECT c FROM Cake c ORDER BY c.createdAt DESC")
    Cake findLatestByCreatedAt();

    Optional<Cake> findByMemberEmail(String email);
    Optional<Cake> findByMemberEmailAndCakeName(String email, String cakeName);
}
