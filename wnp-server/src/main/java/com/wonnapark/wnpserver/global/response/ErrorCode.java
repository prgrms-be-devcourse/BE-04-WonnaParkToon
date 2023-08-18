package com.wonnapark.wnpserver.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    BAD_REQUEST(400, "C000", "잘못된 요청입니다."),
    ENTITY_NOT_FOUND(404, "C001", "해당 엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C999", "서버 내부 에러입니다."),

    // Token
    NOT_VALID_TOKEN(400, "T001", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(404, "T001", "토큰이 존재하지 않습니다.");

    private final int value;
    private final String code;
    private final String message;

    ErrorCode(int value, String code, String message) {
        this.value = value;
        this.code = code;
        this.message = message;
    }

}
