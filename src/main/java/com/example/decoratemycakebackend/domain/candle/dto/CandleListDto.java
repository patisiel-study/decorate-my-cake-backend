package com.example.decoratemycakebackend.domain.candle.dto;

import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.candle.entity.CandleName;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleListDto {
    private Long candleId;
    private CandleName candleName;
    private String candleTitle;
    private String candleContent;
    private String candleUrl;
    private LocalDateTime candleCreatedAt;
    private String writer;
    private boolean isPrivate;

    // Entity -> Dto
    public static CandleListDto from(Candle candle) {
        return new CandleListDto(
                candle.getCandleId(),
                candle.getCandleName(),
                candle.getCandleTitle(),
                candle.getCandleUrl(),
                candle.getCandleContent(),
                candle.getCandleCreatedAt(),
                candle.getWriter(),
                candle.isPrivate()
        );
    }

    // Entity Page -> Dto Page
    public static Page<CandleListDto> from(Page<Candle> candlePage) {
        List<CandleListDto> dtoList = candlePage.stream()
                .map(CandleListDto::from)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, candlePage.getPageable(), candlePage.getTotalElements());
    }

}