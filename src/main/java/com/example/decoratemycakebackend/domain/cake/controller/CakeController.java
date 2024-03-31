package com.example.decoratemycakebackend.domain.cake.controller;

import com.example.decoratemycakebackend.domain.cake.dto.CakeDto;
import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.service.CakeService;
import com.example.decoratemycakebackend.domain.member.dto.MemberCakeDto;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.global.common.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cakes")
public class CakeController {

    private final CakeService cakeService;

    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> findAllCakes(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        LocalDateTime createCreatedAt = LocalDateTime.parse(requestData.get("createCreatedAt"));

        Map<String, Object> response = cakeService.findAllCakes(email, createCreatedAt);
        return ResponseEntity.ok(response);
    }







    /*@PostMapping("/create")
    public ResponseEntity<Response<CakeDto>> createCake(@RequestBody CakeDto cakeDto) {
        Response<CakeDto> response = cakeService.addCake(cakeDto);
        return ResponseEntity.ok(response);
    }*/
}
