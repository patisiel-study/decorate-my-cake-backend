package com.example.decoratemycakebackend.domain.cake.controller;

import com.example.decoratemycakebackend.domain.cake.dto.*;
import com.example.decoratemycakebackend.domain.cake.service.CakeService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cakes")
public class CakeController {

    private final CakeService cakeService;

    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @Operation(summary = "케이크 다 보기", description = "케이크 다 보기")
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<CakeGetResponseDto>>> getCakes(@RequestParam String email) {
        List<CakeGetResponseDto> cakeGetResponseDtos = cakeService.getCakesByEmail(email);
        return ResponseEntity.ok(new ResponseDto<>("케이크 조회가 완료되었습니다.", cakeGetResponseDtos));
    }

    @Operation(summary = "케이크 수정", description = "케이크 수정")
    @PutMapping("/put")
    public ResponseEntity<ResponseDto<CakePutResponseDto>> updateCake(@RequestBody CakePutRequestDto requestDto) {
        CakePutResponseDto responseData = cakeService.updateCake(requestDto);
        ResponseDto<CakePutResponseDto> responseDto = new ResponseDto<>("케이크 설정을 수정했습니다!", responseData);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "케이크 제거", description = "케이크 제거")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<Void>> deleteCake(@RequestBody CakeDeleteRequestDto requestDto) {
        cakeService.deleteCake(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("케이크 삭제가 완료되었습니다.", null));
    }

    @Operation(summary = "케이크 생성", description = "케이크 생성")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<?>> createFriendRequest(@Valid @RequestBody CakeCreateRequestDto request) {
        return ResponseEntity.ok(new ResponseDto<>("케이크 생성이 완료되었습니다.", cakeService.createCake(request)));
    }

    @Operation(summary = "단일 케이크와 하위 캔들 정보 열람", description = "특정 연도 케이크에 대한 정보, 설정, 캔들에 대한 정보 열람 가능")
    @PostMapping("/view")
    public ResponseEntity<ResponseDto<?>> getFriendRequest(@Valid @RequestBody CakeViewRequestDto request) {
        return ResponseEntity.ok(new ResponseDto<>("케이크 및 캔들 열람이 완료되었습니다.", cakeService.getCakeAndCandles(request)));
    }

    @GetMapping("/friends/view")
    public ResponseEntity<List<CakeViewFriendResponseDto>> getCakesForFriend(@PathVariable String friendEmail) {
        List<CakeViewFriendResponseDto> responseDtos = cakeService.getCakesForFriend(friendEmail);
        return ResponseEntity.ok(responseDtos);
    }

}
