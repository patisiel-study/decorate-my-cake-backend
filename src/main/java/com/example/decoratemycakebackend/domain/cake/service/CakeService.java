package com.example.decoratemycakebackend.domain.cake.service;

import com.example.decoratemycakebackend.domain.cake.dto.*;
import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.Setting;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.CandleDto;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CakeService {
    private static final int PAGE_SIZE = 10;
    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;

    //전체 케이크 email로 가져오는거
    public CakeGetResponseDto getCake(CakeGetRequestDto requestDto) {
        // 해당 이메일을 가진 멤버에 대한 유효성 처리
        Member currentMember = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String nickname = currentMember.getNickname();
        LocalDate birthday = currentMember.getBirthday();

        // createdAt을 String으로 받아서 LocalDate로 변환
        //LocalDate createdAt = LocalDate.parse(requestDto.getCreatedAt(), DateTimeFormatter.ofPattern("yyyy"));

        Cake cake = cakeRepository.findByMemberEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        String cakeName = cake.getCakeName();
        LocalDate createdAt = cake.getCreatedAt();

        List<Candle> candles = cake.getCandles();

        List<CandleDto> candleDtoList = candles.stream()
                .map(candle -> new CandleDto(candle.getId(), candle.getCandleName(), candle.getCreatedAt(), candle.getCandleTitle(), candle.getCandleContent(), candle.getPrivate(), candle.getWriter()))
                .collect(Collectors.toList());

        int totalCandle = candleDtoList.size();
        //int totalPage = (int) Math.ceil((double) totalCandle / 10);

        SettingDto settingDto;
        if (cake.getSetting() != null) {
            settingDto = new SettingDto(cake.getSetting().getCandleMakePermission(), cake.getSetting().getCandleViewPermission(), cake.getSetting().isCandleCountPermission());
        } else {
            // Setting 객체가 null인 경우 기본값 사용
            Object CandleViewPermission;
            settingDto = new SettingDto(Setting.CandleMakePermission.ONLY_ME, Setting.CandleViewPermission.ONLY_ME, false);
        }

        //SettingDto settingDto = new SettingDto(cake.getSetting().getCandleMakePermission(), cake.getSetting().getCandleViewPermission(), cake.getSetting().isCandleCountPermission());

        CakeGetResponseDto responseDto = new CakeGetResponseDto(nickname, requestDto.getEmail(), cakeName, birthday, createdAt, candleDtoList, totalCandle, settingDto
        );

        return responseDto;
    }


    public ResponseDto<CakeAddResponseDto> addCake(CakeAddRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String cakeName = requestDto.getCakeName();
        SettingDto settingDto = requestDto.getSetting();
        LocalDate createdAt = requestDto.getCreatedAt();

        Cake cake = new Cake();
        cake.setMember(currentMember);//해당케이크 년도가 안들어가있음
        cake.setCakeName(cakeName); //케이크NAME만 YEAR,MEMEBER 정보가 들어가야함
        cake.setCreatedAt(createdAt);

        cakeRepository.save(cake);

        List<CandleDto> candleList = new ArrayList<>();
        int totalCandle = 0;

        CakeAddResponseDto responseData = new CakeAddResponseDto(currentMember.getNickname(), cakeName, createdAt,
                candleList, totalCandle, requestDto.getSetting());

        return new ResponseDto<>("다가오는 생일의 기쁨을 함께 할 케이크를 만들었습니다. 캔들의 편지는 생일 당일부터 열람 가능합니다!", responseData);
    }

    public CakePutResponseDto updateCake(CakePutRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByMemberEmailAndCakeName(requestDto.getEmail(), requestDto.getCakeName())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        SettingDto settingDto = requestDto.getSetting();

        // Setting 객체가 null인 경우 새로 생성하고 Cake 엔티티에 설정
        if (cake.getSetting() == null) {
            Setting setting = new Setting();
            setting.setCake(cake);
            cake.setSetting(setting);
        }

        // Setting 객체의 속성 업데이트
        cake.getSetting().setCandleMakePermission(settingDto.getCandleMakePermission());
        cake.getSetting().setCandleViewPermission(settingDto.getCandleViewPermission());
        cake.getSetting().setCandleCountPermission(settingDto.isCandleCountPermission());

        Cake updatedCake = cakeRepository.save(cake);

        CakePutResponseDto responseData = new CakePutResponseDto();
        responseData.setCakeName(updatedCake.getCakeName());
        responseData.setSetting(requestDto.getSetting());

        return responseData;
    }

    public ResponseDto<Void> deleteCake(CakeDeleteRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByMemberEmailAndCreatedAt(requestDto.getEmail(), requestDto.getCreatedAt())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        cakeRepository.delete(cake);
        return new ResponseDto<>("케이크 삭제", null); //전체 케이크 다 불러오기 밑에 // 특정 년도에 대한 케이크 정보는 케이크 이미지 라이트 다 // 4가지
    }
}



//1. 우리 케이크 기능이 내 케이크를 만들려면 생일 30일 전까지만 가능 40일 전에는 케이크를 못만든다 화면 생일까지 D-100일 남았습니다 이렇게 나온다 계산? 난 두가지 정보를 알수있음 오늘의날짜 생일 알수있음 내생일이 4월이고 오늘날짜-남은날짜 그냥 보내줌 변수 2.반대로 내생일이 앞이고 오늘날짜 뒤 4월 지금 날짜 6월이면 다시 보여주는데 절대값 30일보다 작으면 간으CREATE 3. 케이크가 아직 없는 경우 내 유저 2024년 데이터 있나?확인 유저 이메일 CREATEDAT확인 없으면 만들어, + 30일 보다 작으면 화면 4. 당해년도 있으면 케이크에 대한 정ㅇ보만 보여줌 캔들에 대한 정보느느 생일당일까지만 볼 수 있음 생일 주인은 당일만 볼 수 있음 절대값이 30보다 적음 해당연도에 대한 유저에 대한 케이크가 있는경우 케이크랑 캔들만 보여줌 내용은 못봄 5. 당일에는 내부에 대한 접근가능