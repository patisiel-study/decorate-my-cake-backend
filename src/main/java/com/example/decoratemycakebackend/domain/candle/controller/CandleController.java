package com.example.decoratemycakebackend.domain.candle.controller;

import com.example.decoratemycakebackend.domain.candle.dto.*;
import com.example.decoratemycakebackend.domain.candle.service.CandleService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "캔들 생성하기", description = "케이크 설정에 따라 캔들 생성하기")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<CandleListDto>> candleAdd(@RequestBody CandleAddRequestDto requestDto) {
        CandleListDto candleListDto = candleService.addCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 성공적으로 생성되었습니다.", candleListDto));
    }

    @Operation(summary = "캔들 전체 가져오기", description = "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
            "다 아니면 메세지에 권한이 없습니다")
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


        //Page<CandleListDto> candlePage = candleService.getCandle(requestDto, pageable);
        messageDto messageDto = candleService.getCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 전체 조회가 완료되었습니다.", messageDto));

    }


    @Operation(summary = "캔들 최신순으로 정렬", description = "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
            "다 아니면 메세지에 권한이 없습니다")
    @GetMapping("/desc")
    public ResponseEntity<ResponseDto<?>> getDescCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();


        //Page<CandleListDto> candlePage = candleService.getDescCandle(requestDto, pageable);
        messageDto messageDto = candleService.getDescCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 최신순으로 조회가 완료되었습니다.", messageDto));

    }


    @Operation(summary = "캔들 년도중 오래된순으로 정렬", description = "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
            "다 아니면 메세지에 권한이 없습니다")
    @GetMapping("/year/asc")
    public ResponseEntity<ResponseDto<?>> getYearAscCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();


        //Page<CandleListDto> candlePage = candleService.getYearAscCandle(requestDto, pageable);
        messageDto messageDto = candleService.getYearAscCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 오래된순으로 조회가 완료되었습니다.", messageDto));

    }
    @Operation(summary = "캔들 오래된순으로 정렬", description = "page: 0, size: 10, sort: candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "candleViewPermission이 ONLY_ME이면 다른 사람 접근 시 '이 케이크는 케이크 주인만 볼 수 있습니다.' 메시지 표시,\n\n" +
            "candleViewPermission이 ONLY_FRIENDS이면 친구가 아닌 사람 접근 시 '이 케이크는 친구만 볼 수 있습니다.' 메시지 표시,\n\n" +
            "그 외의 경우에는 '권한이 없습니다.' 메시지 표시")
    @GetMapping("/asc")
    public ResponseEntity<ResponseDto<?>> getAscCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();

        messageDto messageDto = candleService.getAscCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 오래된순으로 조회가 완료되었습니다.", messageDto));
    }

    @Operation(summary = "캔들 년도별 최신순으로 정렬", description = "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
            "다 아니면 메세지에 권한이 없습니다")
    @GetMapping("/year/desc")
    public ResponseEntity<ResponseDto<?>> getYearCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();


        //Page<CandleListDto> candlePage = candleService.getYearDescCandle(requestDto, pageable);
        messageDto messageDto = candleService.getYearDescCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 년도별 조회가 완료되었습니다.", messageDto));

    }


    @Operation(summary = "캔들 한개 삭제", description = "캔들 id로 삭제하기")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> deleteCandle(@RequestBody CandleDeleteRequestDto requestDto) {
        candleService.deleteCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 삭제되었습니다",null));

    }
}