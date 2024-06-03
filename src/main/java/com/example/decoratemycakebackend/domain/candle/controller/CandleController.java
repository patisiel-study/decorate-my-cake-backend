package com.example.decoratemycakebackend.domain.candle.controller;

import com.example.decoratemycakebackend.domain.candle.dto.*;
import com.example.decoratemycakebackend.domain.candle.service.CandleService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
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

    @Operation(summary = "캔들 전체 가져오기", description = """
        page: 0, size: 10, sort: candleCreatedAt 이렇게 적으면 캔들 출력,
        
        candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,
        
        candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,
        
        다 아니면 메세지에 권한이 없습니다.
        
        totalCandles: 전체 캔들 개수, -1일 경우 비공개.
        
        sortDirection: DESC(최신순), ASC(오래된순)
        """)
    @GetMapping("/list")
    public ResponseEntity<CandleResponseDto> getCandle(
            @RequestParam String email,
            @RequestParam int cakeCreatedYear,
            @RequestParam(defaultValue = "DESC") String sortDirection, // DESC: 내림차순(최신순), ASC: 오름차순(오래된 순)
            @PageableDefault(size = 10, sort = "candleCreatedAt") Pageable pageable
    ) {
        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
                .email(email)
                .cakeCreatedYear(cakeCreatedYear)
                .build();

        // 정렬 방향을 설정
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "candleCreatedAt"));

        CandleResponseDto candleResponseDto = candleService.getCandle(requestDto, sortedPageable);

        return ResponseEntity.ok(candleResponseDto);

    }



//    @Operation(summary = "캔들 년도별 최신순으로 정렬", description = "page: 0, size: 10,sort : candleCreatedAt 이렇게 적으면 캔들 출력,\n\n" +
//            "candleViewPermission이 only_me 다른사람 접근시 이 케이크는 케이크 주인만 볼 수 있습니다,\n\n" +
//            "candleViewPermission이 only_friends 친구 아닌 사람 접근시 이 케이크는 친구만 볼 수 있습니다,\n\n" +
//            "다 아니면 메세지에 권한이 없습니다")
//    @GetMapping("/year/desc")
//    public ResponseEntity<ResponseDto<?>> getYearCandle(
//            @RequestParam String email,
//            @RequestParam int cakeCreatedYear,
//            @PageableDefault(size = 10, sort = "candleCreatedAt", direction = Sort.Direction.DESC) Pageable pageable
//    ) {
//        CandleGetRequestDto requestDto = CandleGetRequestDto.builder()
//                .email(email)
//                .cakeCreatedYear(cakeCreatedYear)
//                .build();
//
//
//        //Page<CandleListDto> candlePage = candleService.getYearDescCandle(requestDto, pageable);
//        CandleResponseDto candleResponseDto = candleService.getYearDescCandle(requestDto, pageable);
//
//        return ResponseEntity.ok(new ResponseDto<>("캔들 년도별 조회가 완료되었습니다.", candleResponseDto));
//
//    }
// TODO: 나의 캔들 연도별 + 정렬 열람은 menu쪽에서 개발하도록 하자. 이의경우, 본인 캔들을 보는 것이므로 케이크 권한과 상관없다.

    @Operation(summary = "캔들 한개 삭제", description = "캔들 id로 삭제하기")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<List<CandleListDto>>> deleteCandle(@RequestBody CandleDeleteRequestDto requestDto) {
        candleService.deleteCandle(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("캔들이 삭제되었습니다",null));

    }
}