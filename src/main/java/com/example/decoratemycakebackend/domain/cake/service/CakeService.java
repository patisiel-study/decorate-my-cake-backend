package com.example.decoratemycakebackend.domain.cake.service;

import com.example.decoratemycakebackend.domain.cake.dto.CakeDto;
import com.example.decoratemycakebackend.domain.cake.dto.SettingDto;
import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.CandleDto;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.common.Response;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CakeService {
    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;

    public CakeService(CakeRepository cakeRepository, MemberRepository memberRepository) {
        this.cakeRepository = cakeRepository;
        this.memberRepository = memberRepository;
    }

    public Map<String, Object> findAllCakes(String email, LocalDateTime createCreatedAt) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.get();

        Cake cake = member.getCakes().stream()
                .filter(c -> c.getCreateCreatedAt().equals(createCreatedAt))
                .findFirst()
                .get();

        Map<String, Object> data = new HashMap<>();
        data.put("nickname", member.getNickname());
        data.put("email", email);
        data.put("birthday", member.getBirthday());

        CakeDto cakeDto = new CakeDto(
                cake.getId(),
                cake.getCakeName(),
                cake.getCreateCreatedAt(),
                cake.getUpdateAt()
        );
        data.put("cakeName", cakeDto.getCakeName());
        data.put("cakeCreatedYear", cakeDto.getCakeCreatedAt().getYear());

        List<CandleDto> candleList = new ArrayList<>();
        for (Candle candle : cake.getCandles()) {
            CandleDto candleDto = new CandleDto(
                    candle.getId(),
                    candle.getCandleName(),
                    candle.getCreateAt(),
                    candle.getCandleTitle(),
                    candle.getPrivate(),
                    candle.getWriter()
            );
            candleList.add(candleDto);
        }
        data.put("candleList", candleList);
        data.put("totalCandle", candleList.size());
        data.put("totalPage", 3);
        data.put("offset", 2);

        SettingDto settingDto = new SettingDto(
                cake.getSetting().getCandleMakePermission(),
                cake.getSetting().getCandleViewPermission(),
                cake.getSetting().isCandleCountPermission()
        );
        data.put("setting", settingDto);

        return data;
    }





    /*public Response<CakeDto> addCake(CakeDto cakeDto) {
        Cake cake = new Cake();
        cake.setCakeName(cakeDto.getCakeName());
        cake.setCreateCreatedAt(LocalDateTime.now());
        cake.setUpdateAt(LocalDateTime.now());
        Cake savedCake = cakeRepository.save(cake);
        CakeDto savedCakeDto = new CakeDto(savedCake.getId(), savedCake.getCakeName(), savedCake.getCreateCreatedAt(), savedCake.getUpdateAt());
        return new Response<>(savedCakeDto, "케이크를 성공적으로 추가했습니다", null);
    }*/
}
