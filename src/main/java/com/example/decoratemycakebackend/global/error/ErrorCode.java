package com.example.decoratemycakebackend.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 정보를 찾을 수 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    VALIDATION_FAILURE(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문이 유효하지 않습니다."),
    ACCOUNT_NOT_MATCHED(HttpStatus.BAD_REQUEST, "로그인 된 회원정보와 다른 유저의 요청입니다."),


    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 잘못되었습니다."),
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구로 추가된 회원입니다."),
    NOT_FRIEND(HttpStatus.BAD_REQUEST, "친구 관계가 아닙니다."),
    CAKE_ALREADY_EXISTS(HttpStatus.NOT_FOUND,"케이크가 이미 있습니다"),
    DUPLICATE_FRIEND_REQUEST(HttpStatus.CONFLICT, "이미 친구 요청을 보낸 상태입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다."),
    CAKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 케이크를 찾을 수 없습니다."),
    CANDLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 캔들을 찾을 수 없습니다."),
    CAKE_ONLY_FRIENDS(HttpStatus.NOT_FOUND, "친구인사람만 볼 수 있습니다."),
    CAKE_ONLY_ME(HttpStatus.NOT_FOUND, "케이크 주인만 볼 수 있습니다"),
    ALREADY_CREATED_CAKE(HttpStatus.OK, "이미 해당 년도의 케이크가 존재합니다."),

    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레시 토큰을 찾지 못했습니다."),
    NOT_MATCHED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "DB의 리프레시 토큰값과 일치하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    EMPTY_FILE_EXCEPTION(HttpStatus.NOT_FOUND, "선택된 파일이 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "친구 요청을 찾을 수 없습니다.");



    private final HttpStatus status;
    private final String message;

}
