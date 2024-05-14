package com.example.decoratemycakebackend.domain.cake.service;

import com.example.decoratemycakebackend.domain.cake.dto.*;
import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.entity.CandleViewPermission;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import com.example.decoratemycakebackend.domain.candle.dto.CandleListDto;
import com.example.decoratemycakebackend.domain.candle.entity.Candle;
import com.example.decoratemycakebackend.domain.candle.repsository.CandleRepository;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequest;
import com.example.decoratemycakebackend.domain.friend.entity.FriendRequestStatus;
import com.example.decoratemycakebackend.domain.friend.repository.FriendRequestRepository;
import com.example.decoratemycakebackend.domain.member.entity.Member;
import com.example.decoratemycakebackend.domain.member.repository.MemberRepository;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import com.example.decoratemycakebackend.global.util.ResponseDto;
import com.example.decoratemycakebackend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.decoratemycakebackend.global.util.ValidationUtil.validateEmailMatch;

@Service
@RequiredArgsConstructor
public class CakeService {
    private static final int PAGE_SIZE = 10;
    private final CakeRepository cakeRepository;
    private final MemberRepository memberRepository;
    private final CandleRepository candleRepository;
    private final FriendRequestRepository friendRequestRepository;

    //전체 케이크 email로 가져오는거 creatat 필요없음
    public List<CakeGetResponseDto> getCakesByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Cake> cakes = cakeRepository.findAllByMemberEmail(email);

        List<CakeGetResponseDto> responseDtos = new ArrayList<>();
        

        for (Cake cake : cakes) {
            String nickname = member.getNickname();
            LocalDate birthday = member.getBirthday();
            String cakeName = cake.getCakeName();
            LocalDate createdAt = cake.getCreatedAt();
            List<Candle> candles = cake.getCandles();
            List<CandleListDto> candleListDtoList = candles.stream()
                    .map(candle -> new CandleListDto(candle.getCandleId(), candle.getTitle(), candle.getContent(), candle.getCandleCreatedAt(), candle.getWriter(), candle.isPrivate(),candle.getTotalcandlecount()))
                    .collect(Collectors.toList());
            int totalCandle = candleListDtoList.size();

            CakeGetResponseDto responseDto = new CakeGetResponseDto(
                    nickname,
                    email,
                    cakeName,
                    birthday,
                    createdAt,
                    candleListDtoList,
                    totalCandle,
                    cake.getCandleCreatePermission(),
                    cake.getCandleViewPermission(),
                    cake.getCandleCountPermission()
            );

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    /*
    //모든 케이크 list로 2022 2023 dto로 반환 케이크에 대한 정보를 캔들 리스트로 케이크 리스트로
    public ResponseDto<CakeAddResponseDto> addCake(CakeAddRequestDto requestDto) {
        Member currentMember = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = Cake.builder()
                .cakeName(requestDto.getCakeName())
                .member(currentMember)
                .createdYear(LocalDateTime.now().getYear())
                .candleCreatePermission(requestDto.getCandleCreatePermission())
                .candleViewPermission(requestDto.getCandleViewPermission())
                .candleCountPermission(requestDto.getCandleCountPermission())
                .candles(Collections.emptyList())
                .build();

        cakeRepository.save(cake);

        List<CandleListDto> candleList = new ArrayList<>();
        int totalCandle = 0;

        CakeAddResponseDto.CakeSetting setting = CakeAddResponseDto.CakeSetting.builder()
                .candleCreatePermission(requestDto.getCandleCreatePermission())
                .candleViewPermission(requestDto.getCandleViewPermission())
                .candleCountPermission(requestDto.getCandleCountPermission())
                .build();

        CakeAddResponseDto responseData = new CakeAddResponseDto(currentMember.getNickname(), cake.getCakeName(), cake.getCreatedYear(), candleList, totalCandle, setting);

        return new ResponseDto<>("다가오는 생일의 기쁨을 함께 할 케이크를 만들었습니다. 캔들의 편지는 생일 당일부터 열람 가능합니다!", responseData);
    }
    */


    public CakePutResponseDto updateCake(CakePutRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakecreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        // 권한 필드 업데이트
        cake.setCandleCreatePermission(requestDto.getCandleCreatePermission());
        cake.setCandleViewPermission(requestDto.getCandleViewPermission());
        cake.setCandleCountPermission(requestDto.getCandleCountPermission());

        Cake updatedCake = cakeRepository.save(cake);

        CakePutResponseDto responseData = new CakePutResponseDto();
        responseData.setCakeName(updatedCake.getCakeName());
        responseData.setCandleCreatePermission(updatedCake.getCandleCreatePermission());
        responseData.setCandleViewPermission(updatedCake.getCandleViewPermission());
        responseData.setCandleCountPermission(updatedCake.getCandleCountPermission());

        return responseData;
    }


    public ResponseDto<Void> deleteCake(CakeDeleteRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Cake cake = cakeRepository.findByEmailAndCreatedYear(requestDto.getEmail(), requestDto.getCakecreatedYear())
                .orElseThrow(() -> new CustomException(ErrorCode.CAKE_NOT_FOUND));

        cakeRepository.delete(cake);

        return new ResponseDto<>("케이크 삭제", null);
    }

    public CakeCreateResponseDto createCake(CakeCreateRequestDto request) {
        // 프론트에서 보낸 email과 로그인 된 유저의 email 일치 여부 확인
        String email = SecurityUtil.getCurrentUserEmail();
        validateEmailMatch(email, request.getEmail());

        // 멤버 정보 DB에서 조회
        Member member = getMember(email);

        // 해당 연도에 대한 케이크가 이미 존재하는지 확인
        int createdYear = request.getCreatedYear();
        if (cakeRepository.existsByEmailAndCreatedYear(email, createdYear)) {
            throw new CustomException(ErrorCode.CAKE_ALREADY_EXISTS);
        }

        // 케이크 정보 생성
        Cake cake = createCake(request, member, email, createdYear);

        // DB에 정보 저장후 멤버 정보 업데이트
        saveCakeAndUpdateMember(cake, member);

        // 케이크 설정 정보 생성
        CakeCreateResponseDto.CakeSetting cakeSetting = createCakeSetting(cake);

        // 케이크 정보 및 설정 정보 반환
        return createCakeCreateResponseDto(cakeSetting, cake);
    }
    public CakeViewResponseDto getCakeAndCandles(CakeViewRequestDto request) {
        // 친구의 케이크를 조회할 수도 있으므로 로그인 한 유저의 이메일과 일치 여부 확인하지 않음
        String email = request.getEmail();

        Member member = getMember(email);
        Cake cake = getCake(email, request.getCakeCreatedYear());

        // 케이크가 존재하지 않는 경우 예외 처리
        if (cake == null) {
            throw new CustomException(ErrorCode.CAKE_NOT_FOUND);
        }

        // 생일까지 남은기간 계산
        LocalDate today = LocalDate.now();
        LocalDate birthday = member.getBirthday();
        LocalDate nextBirthday = getNextBirthday(today, birthday);

        long daysUntilBirthday = ChronoUnit.DAYS.between(today, nextBirthday);
        int age = nextBirthday.getYear() - birthday.getYear();

        if (isBirthdayToday(daysUntilBirthday)) {
            return getBirthdayCakeViewResponseDto(member, age, cake);
        } else {
            return getBeforeBirthdayCakeViewResponseDto(member, birthday, age, daysUntilBirthday, cake);
        }
    }


    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Cake createCake(CakeCreateRequestDto request, Member member, String email, int createdYear) {
        return Cake.builder()
                .cakeName(request.getCakeName())
                .member(member)
                .email(email)
                .createdYear(createdYear)
                .candleCreatePermission(request.getCandleCreatePermission())
                .candleViewPermission(request.getCandleViewPermission())
                .candleCountPermission(request.getCandleCountPermission())
                .candles(Collections.emptyList())
                .build();
    }

    private void saveCakeAndUpdateMember(Cake cake, Member member) {
        cakeRepository.save(cake);
        member.getCakes().add(cake);
        memberRepository.save(member);
    }

    private CakeCreateResponseDto.CakeSetting createCakeSetting(Cake cake) {
        return CakeCreateResponseDto.CakeSetting.builder()
                .candleCreatePermission(cake.getCandleCreatePermission())
                .candleViewPermission(cake.getCandleViewPermission())
                .candleCountPermission(cake.getCandleCountPermission())
                .build();
    }

    private CakeCreateResponseDto createCakeCreateResponseDto(CakeCreateResponseDto.CakeSetting cakeSetting, Cake cake) {
        return CakeCreateResponseDto.builder()
                .setting(cakeSetting)
                .cakeName(cake.getCakeName())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(cake.getCandles())
                .nickname(cake.getMember().getNickname())
                .build();
    }

    private Cake getCake(String email, int cakeCreatedYear) {
        return cakeRepository.findByEmailAndCreatedYear(email, cakeCreatedYear)
                .orElse(null);
    }

    private LocalDate getNextBirthday(LocalDate today, LocalDate birthday) {
        LocalDate thisYearBirthday = LocalDate.of(today.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth());
        return today.isBefore(thisYearBirthday) ? thisYearBirthday : thisYearBirthday.plusYears(1);
    }

    private boolean isBirthdayToday(long daysUntilBirthday) {
        return daysUntilBirthday == 365;
    }

    private CakeViewResponseDto getBirthdayCakeViewFriendResponseDto(Member member, int age, Cake cake) {
        // 생일 당일인데 케이크 없으면 만들도록 유도
        if (cake == null) {
            return CakeViewResponseDto.builder()
                    .nickname(member.getNickname())
                    .birthday(member.getBirthday().toString())
                    .message(recommandToCreateCake(member, age))
                    .build();
        }
        // 생일 당일에 케이크가 있으면 모든 캔들 정보 공개
        List<CandleListDto> candleList = cake.getCandles().stream()
                .map(this::toCandleListDto)
                .collect(Collectors.toList());

        CakeViewResponseDto.CakeSetting cakeSetting = createCakeViewResponseDtoCakeSetting(cake);

        return CakeViewResponseDto.builder()
                .message(getBirthdayMessage(member, age))
                .nickname(member.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(member.getBirthday().toString())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(candleList)
                .setting(cakeSetting)
                .build();
    }

    private CakeViewResponseDto getBirthdayCakeViewResponseDto(Member member, int age, Cake cake) {
        // 생일 당일인데 케이크 없으면 만들도록 유도
        if (cake == null) {
            return CakeViewResponseDto.builder()
                    .nickname(member.getNickname())
                    .birthday(member.getBirthday().toString())
                    .message(recommandToCreateCake(member, age))
                    .build();
        }
        // 생일 당일에 케이크가 있으면 모든 캔들 정보 공개
        List<CandleListDto> candleList = cake.getCandles().stream()
                .map(this::toCandleListDto)
                .collect(Collectors.toList());

        CakeViewResponseDto.CakeSetting cakeSetting = createCakeViewResponseDtoCakeSetting(cake);

        return CakeViewResponseDto.builder()
                .message(getBirthdayMessage(member, age))
                .nickname(member.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(member.getBirthday().toString())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(candleList)
                .setting(cakeSetting)
                .build();
    }

    private CakeViewResponseDto getBeforeBirthdayCakeViewResponseDto(Member member, LocalDate birthday, int age, long daysUntilBirthday, Cake cake) {
        // D-Day가 30일보다 많이 남은경우
        if (daysUntilBirthday > 30) {
            return CakeViewResponseDto.builder()
                    .nickname(member.getNickname())
                    .birthday(birthday.toString())
                    .message(getDDayMessage(daysUntilBirthday))
                    .build();
        }
        // D-Day가 30일 이하로 남은 경우
        // 케이크 안 만들었다면 케이크 만들도록 유도
        if (cake == null) {
            return CakeViewResponseDto.builder()
                    .nickname(member.getNickname())
                    .birthday(birthday.toString())
                    .message(recommandToCreateCake(member, age))
                    .build();
        }
        // 케이크 만들었다면 캔들 정보 일부만 공개
        List<CandleListDto> candleList = cake.getCandles().stream()
                .map(candle -> CandleListDto.builder()
                        //.candleName(candle.getName())
                        .writer(candle.getWriter())
                        .build())
                .collect(Collectors.toList());

        CakeViewResponseDto.CakeSetting cakeSetting = createCakeViewResponseDtoCakeSetting(cake);

        return CakeViewResponseDto.builder()
                .nickname(member.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(member.getBirthday().toString())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(candleList)
                .setting(cakeSetting)
                .build();
    }

    private CakeViewResponseDto.CakeSetting createCakeViewResponseDtoCakeSetting(Cake cake) {
        return CakeViewResponseDto.CakeSetting.builder()
                .candleCreatePermission(cake.getCandleCreatePermission())
                .candleViewPermission(cake.getCandleViewPermission())
                .candleCountPermission(cake.getCandleCountPermission())
                .build();
    }

    private String recommandToCreateCake(Member member, int age) {
        return member.getNickname() + "님의 " + age + "살 생일 케이크를 만들어 보세요!";
    }

    private String getBirthdayMessage(Member member, int age) {
        return member.getNickname() + "님의 " + age + "살 생일을 축하합니다!!";
    }

    private String getDDayMessage(long daysUntilBirthday) {
        return "생일까지 D-" + daysUntilBirthday + "일 남았습니다!";
    }

    private CandleListDto toCandleListDto(Candle candle) {
        return CandleListDto.builder()
                .candleId(candle.getCandleId())
                //.candleName(candle.getName())
                .candleTitle(candle.getTitle())
                .candleContent(candle.getContent())
                .candleCreatedAt(candle.getCandleCreatedAt())
                .writer(candle.getWriter())
                .isPrivate(candle.isPrivate())
                .build();
    }


    // 친구의 케이크 정보 조회
    public List<CakeViewFriendResponseDto> getCakesForFriend(String friendEmail) { //친구 이메일 입력 받기, 친구 관계인지 확인 친구 아니면 예외
        Member currentMember = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member friend = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 두 멤버가 친구 관계인지 확인
        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(friend, currentMember, FriendRequestStatus.ACCEPTED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FRIEND));

        Set<String> processedFriendEmails = new HashSet<>(); //중복방지
        List<CakeViewFriendResponseDto> responseDtos = new ArrayList<>();

        for (Cake cake : friend.getCakes()) {
            if (processedFriendEmails.contains(friend.getEmail())) {
                continue; // 이미 처리된 친구의 케이크는 건너뜀
            }
            processedFriendEmails.add(friend.getEmail());
            CakeViewFriendRequestDto request = CakeViewFriendRequestDto.builder()
                    .email(friendEmail)
                    .cakeCreatedYear(cake.getCreatedYear())
                    //.isPrivate(cake.isPrivate())
                    .build();

            CakeViewFriendResponseDto response = getCakeViewFriendResponse(request, friend, cake);
            responseDtos.add(response);
        }

        return responseDtos;
    }

    private CakeViewFriendResponseDto getCakeViewFriendResponse(CakeViewFriendRequestDto request, Member friend, Cake cake) { //케이크 공개 여부 확인 비공개이면 반환 공개이면 if문에서 고르기
        if (request.isPrivate()) {
            return CakeViewFriendResponseDto.builder()
                    .nickname(friend.getNickname())
                    .message("친구의 케이크가 비공개로 설정되었습니다.")
                    .build();
        }

        int cakeCreatedYear = cake.getCreatedYear();
        LocalDate friendBirthday = friend.getBirthday();
        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = getNextBirthday(today, friendBirthday);
        long daysUntilBirthday = ChronoUnit.DAYS.between(today, nextBirthday);
        int age = nextBirthday.getYear() - friendBirthday.getYear();

        if (isBirthdayToday(daysUntilBirthday)) {
            if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) { //당일 모든 캔들 정보 포함한 응답 객체 반환
                return getFriendBirthdayCakeViewResponseDto(friend, age, cake);
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) { //비공개 캔들 제외하고 반환
                return getFriendPartialCakeViewResponseDto(friend, age, cake);
            } else {
                return getFriendMinimalCakeViewResponseDto(friend, friendBirthday, cakeCreatedYear, cake); //나만
            }
        } else if (daysUntilBirthday > 30) {
            return getDDayMessageFriendResponseDto(friend, friendBirthday, daysUntilBirthday);
        } else {
            if (cake.getCandleViewPermission() == CandleViewPermission.ONLY_FRIENDS) { //30일 전 친구만
                return getFriendPartialCakeViewResponseDto(friend, age, cake);
            } else if (cake.getCandleViewPermission() == CandleViewPermission.ANYONE) { //누구나
                return getFriendBirthdayCakeViewResponseDto(friend, age, cake);
            } else {
                return getFriendMinimalCakeViewResponseDto(friend, friendBirthday, cakeCreatedYear, cake); //나만
            }
        }
    }

    //위에 cakeviewfriendresponsedto 생성
    private CakeViewFriendResponseDto getFriendBirthdayCakeViewResponseDto(Member friend, int age, Cake cake) {
        CakeViewFriendResponseDto.CakeSetting cakeSetting = createCakeViewFriendResponseDtoCakeSetting(cake); //setting 반환

        List<CandleListDto> candleList = cake.getCandles().stream() //공개만
                .map(this::toCandleListDto)
                .filter(candleListDto -> !candleListDto.isPrivate())
                .collect(Collectors.toList());

        return CakeViewFriendResponseDto.builder()
                .message(getFriendBirthdayMessage(friend, age))
                .nickname(friend.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(friend.getBirthday().toString())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(candleList)
                .setting(cakeSetting)
                .build();
    }


    private CakeViewFriendResponseDto getFriendPartialCakeViewResponseDto(Member friend, int age, Cake cake) { //CandleViewPermission.ONLY_FRIENDS 일때만
        CakeViewFriendResponseDto.CakeSetting cakeSetting = createCakeViewFriendResponseDtoCakeSetting(cake);

        List<CandleListDto> candleList = cake.getCandles().stream()
                .map(candle -> CandleListDto.builder()
                        //.candleName(candle.getName())
                        .writer(candle.getWriter())
                        .isPrivate(candle.isPrivate()) //즉 비공개
                        .build())
                .filter(candleListDto -> !candleListDto.isPrivate())
                .collect(Collectors.toList());

        return CakeViewFriendResponseDto.builder()
                .nickname(friend.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(friend.getBirthday().toString())
                .cakeCreatedYear(cake.getCreatedYear())
                .candleList(candleList)
                .setting(cakeSetting)
                .build();
    }

    private CakeViewFriendResponseDto getFriendMinimalCakeViewResponseDto(Member friend, LocalDate birthday, int cakeCreatedYear, Cake cake) { //CandleViewPermission.ONLY_ME
        CakeViewFriendResponseDto.CakeSetting cakeSetting = createCakeViewFriendResponseDtoCakeSetting(cake);

        List<CandleListDto> candleList = cake.getCandles().stream()
                .map(candle -> CandleListDto.builder()
                        //.candleName(candle.getName())
                        .writer(candle.getWriter())
                        .build())
                .collect(Collectors.toList());

        return CakeViewFriendResponseDto.builder()
                .nickname(friend.getNickname())
                .cakeName(cake.getCakeName())
                .birthday(birthday.toString())
                .cakeCreatedYear(cakeCreatedYear)
                .setting(cakeSetting)
                .build();
    }

    private CakeViewFriendResponseDto getDDayMessageFriendResponseDto(Member friend, LocalDate birthday, long daysUntilBirthday) { //메세지 포함
        return CakeViewFriendResponseDto.builder()
                .nickname(friend.getNickname())
                .birthday(birthday.toString())
                .message(getDDayMessage(daysUntilBirthday))
                .build();
    }

    private CakeViewFriendResponseDto.CakeSetting createCakeViewFriendResponseDtoCakeSetting(Cake cake) { //케이크의 설정 정보
        return CakeViewFriendResponseDto.CakeSetting.builder()
                .candleCreatePermission(cake.getCandleCreatePermission())
                .candleViewPermission(cake.getCandleViewPermission())
                .candleCountPermission(cake.getCandleCountPermission())
                .build();
    }


    private String getFriendBirthdayMessage(Member friend, int age) { //생일 메세지
        return friend.getNickname() + "님의 " + age + "살 생일을 축하합니다!!";
    }


}
//1. 우리 케이크 기능이 내 케이크를 만들려면 생일 30일 전까지만 가능 40일 전에는 케이크를 못만든다 화면 생일까지 D-100일 남았습니다 이렇게 나온다 계산? 난 두가지 정보를 알수있음 오늘의날짜 생일 알수있음 내생일이 4월이고 오늘날짜-남은날짜 그냥 보내줌 변수 2.반대로 내생일이 앞이고 오늘날짜 뒤 4월 지금 날짜 6월이면 다시 보여주는데 절대값 30일보다 작으면 간으CREATE 3. 케이크가 아직 없는 경우 내 유저 2024년 데이터 있나?확인 유저 이메일 CREATEDAT확인 없으면 만들어, + 30일 보다 작으면 화면 4. 당해년도 있으면 케이크에 대한 정ㅇ보만 보여줌 캔들에 대한 정보느느 생일당일까지만 볼 수 있음 생일 주인은 당일만 볼 수 있음 절대값이 30보다 적음 해당연도에 대한 유저에 대한 케이크가 있는경우 케이크랑 캔들만 보여줌 내용은 못봄 5. 당일에는 내부에 대한 접근가능

//친구꺼 확인하려염ㄴ api 두번 만들어야함 emial을 받아옴 시큐리티