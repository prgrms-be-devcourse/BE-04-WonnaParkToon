package com.wonnapark.wnpserver.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;

@Getter
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<FieldErrorInfo> errors;

    private ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = null;
    }

    private ErrorResponse(int status, String code, String message, List<FieldErrorInfo> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse create(ErrorCode detail) {
        return new ErrorResponse(detail.getValue(),
                detail.getCode(),
                detail.getMessage());
    }

    public static ErrorResponse create(ErrorCode detail, BindingResult bindingResult) {
        return new ErrorResponse(detail.getValue(),
                detail.getCode(),
                detail.getMessage(),
                FieldErrorInfo.create(bindingResult));
    }

}
