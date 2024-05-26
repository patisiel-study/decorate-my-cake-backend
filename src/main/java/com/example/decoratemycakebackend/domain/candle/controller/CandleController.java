package com.example.decoratemycakebackend.domain.candle.controller;

import com.example.decoratemycakebackend.domain.candle.dto.CandleAddRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleDeleteRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleGetRequestDto;
import com.example.decoratemycakebackend.domain.candle.dto.CandleListDto;
import com.example.decoratemycakebackend.domain.candle.service.CandleService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "캔들 생성하기", description = "케이크 설정에 따라 캔들 생성하기")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<CandleListDto>> candleAdd(@RequestBody CandleAddRequestDto requestDto) {
        CandleListDto candleListDto = candleService.addCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 성공적으로 생성되었습니다.", candleListDto));
    }
    @Operation(summary = "캔들 전체 가져오기", description = "페이지 0 만약 사이즈10이 넘어가면 1로 적으면 됨,\n\n" +
            "사이즈 10 (한 페이지에 10개 출력 고정), \" \" 정렬은 최신순\n\n" +
            "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "만약 candleViewPermission이 only_me인데 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends인데 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
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


        Page<CandleListDto> candlePage = candleService.getCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 전체 조회가 완료되었습니다.", candlePage));

    }


    @Operation(summary = "캔들 최신순으로 정렬", description = "페이지 0 만약 사이즈10이 넘어가면 1로 적으면 됨,\n\n" +
            "사이즈 10 (한 페이지에 10개 출력 고정), \" \" 정렬은 최신순\n\n" +
            "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "만약 candleViewPermission이 only_me인데 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends인데 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
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


        Page<CandleListDto> candlePage = candleService.getDescCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 최신순으로 조회가 완료되었습니다.", candlePage));

    }


    @Operation(summary = "캔들 년도중 오래된순으로 정렬", description = "페이지 0 만약 사이즈10이 넘어가면 1로 적으면 됨,\n\n" +
            "사이즈 10 (한 페이지에 10개 출력 고정), \" \" 정렬은 최신순\n\n" +
            "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "만약 candleViewPermission이 only_me인데 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends인데 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
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


        Page<CandleListDto> candlePage = candleService.getYearAscCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 오래된순으로 조회가 완료되었습니다.", candlePage));

    }
    @Operation(summary = "캔들 오래된순으로 정렬", description = "페이지 0 만약 사이즈10이 넘어가면 1로 적으면 됨,\n\n" +
            "사이즈 10 (한 페이지에 10개 출력 고정), \" \" 정렬은 최신순\n\n" +
            "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "만약 candleViewPermission이 only_me인데 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends인데 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
            "다 아니면 메세지에 권한이 없습니다")
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


        Page<CandleListDto> candlePage = candleService.getAscCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 오래된순으로 조회가 완료되었습니다.", candlePage));

    }

    @Operation(summary = "캔들 년도별 최신순으로 정렬", description = "페이지 0 만약 사이즈10이 넘어가면 1로 적으면 됨,\n\n" +
            "사이즈 10 (한 페이지에 10개 출력 고정), \" \" 정렬은 최신순\n\n" +
            "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
            "만약 candleViewPermission이 only_me인데 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
            "candleViewPermission이 only_friends인데 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
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


        Page<CandleListDto> candlePage = candleService.getYearDescCandle(requestDto, pageable);

        return ResponseEntity.ok(new ResponseDto<>("캔들 년도별 조회가 완료되었습니다.", candlePage));

    }


    @Operation(summary = "캔들 하나 삭제", description = "캔들 id로 삭제하기")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> deleteCandle(@RequestBody CandleDeleteRequestDto requestDto) {
        candleService.deleteCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 삭제되었습니다",null));

    }
}