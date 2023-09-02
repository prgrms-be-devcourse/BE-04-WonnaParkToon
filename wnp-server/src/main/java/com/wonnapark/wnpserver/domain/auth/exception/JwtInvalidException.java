package com.wonnapark.wnpserver.domain.auth.exception;

import com.wonnapark.wnpserver.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class JwtInvalidException extends RuntimeException {

    private final ErrorCode errorCode;

    public JwtInvalidException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
