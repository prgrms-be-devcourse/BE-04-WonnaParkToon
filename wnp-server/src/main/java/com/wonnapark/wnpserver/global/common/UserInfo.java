package com.wonnapark.wnpserver.global.common;

public record UserInfo(
        Long userId,
        int age
) {
    public static UserInfo from(String[] extractedSubject) {
        return new UserInfo(Long.valueOf(extractedSubject[0]), Integer.parseInt(extractedSubject[1]));
    }
}
