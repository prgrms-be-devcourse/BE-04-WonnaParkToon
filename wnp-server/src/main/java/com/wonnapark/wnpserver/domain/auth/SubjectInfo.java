package com.wonnapark.wnpserver.domain.auth;

public record SubjectInfo(
        Long userID,
        String birthYear
){
    public static SubjectInfo from(String[] extractedSubject) {
        return new SubjectInfo(Long.valueOf(extractedSubject[0]), extractedSubject[1]);
    }
}
