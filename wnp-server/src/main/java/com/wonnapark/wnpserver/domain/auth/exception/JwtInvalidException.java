package com.wonnapark.wnpserver.domain.auth.exception;

public class JwtInvalidException extends RuntimeException {
    private static final String INVALID_JWT_MESSAGE = "유요하지 않은 토큰 정보입니다.";

    public JwtInvalidException() {
        super(INVALID_JWT_MESSAGE);
    }

    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause);

    }
}
