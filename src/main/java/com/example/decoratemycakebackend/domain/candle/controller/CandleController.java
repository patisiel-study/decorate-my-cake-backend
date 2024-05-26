package com.example.decoratemycakebackend.domain.candle.controller;

import com.example.decoratemycakebackend.domain.candle.dto.*;
import com.example.decoratemycakebackend.domain.candle.service.CandleService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candle")
public class CandleController {

    private final CandleService candleService;

    public CandleController(CandleService candleService) {
        this.candleService = candleService;
    }

    @Operation(summary = "캔들 생성", description = "캔들 생성")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<CandleListDto>> candleAdd(@RequestBody CandleAddRequestDto requestDto) {
        ResponseDto<CandleListDto> responseDto = candleService.addCandle(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "캔들 친구요청으로 생성", description = "캔들 친구요청")
    @PostMapping("/create/friends")
    public ResponseEntity<ResponseDto<CandleListDto>> createCandleForFriends(@Valid @RequestBody CandleAddFriendsRequestDto request) {
        return ResponseEntity.ok(candleService.addCandleFriends(request));
    }

    @Operation(summary = "캔들 전체 가져오기", description = "캔들 가져오기")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<?>> getCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();


        Page<CandleListDto> candlePage = candleService.getCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 전체 조회가 완료되었습니다.", candlePage));

    }

    @Operation(summary = "캔들 최신순으로 가져오기", description = "캔들 최신순으로 가져오기")
    @GetMapping("/desc")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> getDescCandle(@RequestParam String email, @RequestParam int cakecreatedYear) {
        CandleGetRequestDto requestDto = new CandleGetRequestDto(email, cakecreatedYear);
        ResponseDto<List<CandleListDto>> responseDto = candleService.getDescCandle(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "캔들 년도별로 가져오기", description = "캔들 년도별로 최신순으로 가져오기")
    @GetMapping("/year")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> getYearCandle(@ModelAttribute CandleGetRequestDto requestDto) {
        ResponseDto<List<CandleListDto>> responseDto = candleService.getYearCandle(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> deleteCandle(@RequestBody CandleDeleteRequestDto requestDto) {
        candleService.deleteCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 삭제되었습니다",null));

    }
}