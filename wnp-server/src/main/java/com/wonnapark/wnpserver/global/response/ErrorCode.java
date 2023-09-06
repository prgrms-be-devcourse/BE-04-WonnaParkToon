package com.wonnapark.wnpserver.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    BAD_REQUEST(400, "C000", "잘못된 요청입니다."),
    ENTITY_NOT_FOUND(404, "C001", "해당 엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C999", "서버 내부 에러입니다."),

    // Token
    EXPIRED_TOKEN(400, "T001", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(400, "T002", "잘못된 형식의 토큰입니다."),
    WRONG_SIGNATURE_TOKEN(400, "T003", "변조된 토큰입니다. 토큰이 탈취되었을 수도 있습니다."),
    TOKEN_NOT_FOUND(404, "T001", "토큰을 찾을 수 없습니다."),

    // User
    USER_NOT_FOUND(404, "U001", "유저를 찾을 수 없습니다.");


    private final int value;
    private final String code;
    private final String message;

    ErrorCode(int value, String code, String message) {
        this.value = value;
        this.code = code;
        this.message = message;
    }

}
