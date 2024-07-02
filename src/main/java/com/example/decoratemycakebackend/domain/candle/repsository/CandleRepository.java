package com.example.decoratemycakebackend.domain.candle.repsository;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandleRepository extends JpaRepository<Candle, Long> {

    Optional<Candle> findById(Long candleId);

    Page<Candle> findByCake(Cake cake, Pageable pageable);

    Page<Candle> findAllByWriterEmail(String writerEmail, Pageable pageable);

}
