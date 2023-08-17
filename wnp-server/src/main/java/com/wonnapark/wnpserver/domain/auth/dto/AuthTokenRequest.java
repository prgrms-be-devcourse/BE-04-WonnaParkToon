package com.wonnapark.wnpserver.domain.auth.dto;

public record AuthTokenRequest (
        Long userId,
        String birthYear
){
}
