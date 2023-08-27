package com.wonnapark.wnpserver.global.common;

public record UserInfo(
        Long userId,
        String birthYear
) {
    public static UserInfo from(String[] extractedSubject) {
        return new UserInfo(Long.valueOf(extractedSubject[0]), extractedSubject[1]);
    }
}
