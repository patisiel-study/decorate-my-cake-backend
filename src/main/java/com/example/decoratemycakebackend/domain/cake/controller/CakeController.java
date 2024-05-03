package com.example.decoratemycakebackend.domain.cake.controller;

import com.example.decoratemycakebackend.domain.cake.dto.*;
import com.example.decoratemycakebackend.domain.cake.service.CakeService;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cakes")
public class CakeController {

    private final CakeService cakeService;

    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<CakeGetResponseDto>> getCake(@RequestParam String email, @RequestParam LocalDate createdAt) {
        CakeGetRequestDto requestDto = new CakeGetRequestDto(email);
        CakeGetResponseDto cakeGetResponseDto = cakeService.getCake(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("케이크 조회가 완료되었습니다.", cakeGetResponseDto));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<CakeAddResponseDto>> createCake(@RequestBody CakeAddRequestDto requestDto) {
        ResponseDto<CakeAddResponseDto> response = cakeService.addCake(requestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cake")
    public ResponseEntity<CakePutResponseDto> updateCake(@RequestBody CakePutRequestDto requestDto) {
        CakePutResponseDto responseDto = cakeService.updateCake(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<Void>> deleteCake(@RequestBody CakeDeleteRequestDto requestDto) {
        cakeService.deleteCake(requestDto);
        return ResponseEntity.ok(new ResponseDto<>("케이크 삭제가 완료되었습니다.", null));
    }

}
