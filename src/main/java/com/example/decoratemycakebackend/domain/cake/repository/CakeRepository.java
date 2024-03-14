package com.example.decoratemycakebackend.domain.cake.repository;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeRepository extends JpaRepository<Cake, Long> {
}
