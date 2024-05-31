package com.example.decoratemycakebackend.domain.candle.repsository;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandleRepository extends JpaRepository<Candle, Long> {
    List<Candle> findByCake(Cake cake);

    Optional<Candle> findById(Long candleId);

    @Query("SELECT COUNT(c) FROM Candle c")
    long totalcandlecount();

    Page<Candle> findByCakeOrderByCandleCreatedAtDesc(Cake cake, Pageable pageable);

    Page<Candle> findByCakeOrderByCandleCreatedAtAsc(Cake cake, Pageable pageable);

    //년도
    @Query("SELECT c FROM Candle c WHERE c.cake = :cake ORDER BY YEAR(c.candleCreatedAt) DESC, c.candleCreatedAt DESC")
    Page<Candle> findByCakeOrderByYearAndCandleCreatedAtDesc(@Param("cake") Cake cake, Pageable pageable);

    @Query("SELECT c FROM Candle c WHERE c.cake = :cake ORDER BY YEAR(c.candleCreatedAt) ASC, c.candleCreatedAt ASC")
    Page<Candle> findByCakeOrderByYearAndCandleCreatedAtAsc(@Param("cake") Cake cake, Pageable pageable);

    Page<Candle> findByCake(Cake cake, Pageable pageable);

    long countByCake(Cake cake);

}
