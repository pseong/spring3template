package com.pseong.spring3template.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "토큰이 존재하지 않습니다."),
    INVALID_JWT(false, 2002, "토큰이 만료되었습니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    REQUEST_JWT_ERROR(false,2004,"토큰 입력형식이 잘못되었습니다."),

    UNAUTHORIZED(false, 2005, "인증되지 않은 요청입니다."),
    FORBIDDEN(false, 2006, "권한이 없습니다."),

    USER_NOT_FOUND(false, 2007, "유저를 찾을 수 없습니다."),

    INVALID_TOKEN(false, 2007, "유효하지 않은 토큰입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_LOGINID(false, 2015, "아이디를 입력해주세요."),
    POST_USERS_EMPTY_NICKNAME(false, 2016, "닉네임을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2017, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_PHONENUM(false, 2018, "핸드폰번호를 입력해주세요."),

    POST_USERS_INVALID_LOGINID(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_LOGINID(false,2019,"중복된 아이디입니다."),

    // Log-in
    POST_USERS_DISABLED_USER(false, 2020, "탈퇴한 회원입니다."),

    // File
    INVALID_EXT(false, 2021, "지원하지 않는 확장자입니다."),




    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_PASSWORD_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    FAILED_TO_GOOGLE_LOGIN(false,3015,"가입된 구글 계정이 없습니다."),
    FAILED_TO_KAKAO_LOGIN(false,3016,"카카오 로그인 에러"),
    DISABLED_USER(false,3017,"삭제된 사용자입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    EXISTS_USERNAME(false,4014,"중복된 닉네임입니다."),

    MODIFY_FAIL_DELETEUSER(false,4015,"유저 삭제에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    JSON_ERROR(false, 4040, "JSON 파싱에 실패하였습니다."),

    INTERNAL_SERVER_ERROR(false, 6000, "서버 오류");
    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
