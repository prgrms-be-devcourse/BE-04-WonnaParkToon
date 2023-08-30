package com.wonnapark.wnpserver.domain.webtoon.exception;

public class UnderageAccessDeniedException extends RuntimeException {
    public UnderageAccessDeniedException(String message) {
        super(message);
    }
}
