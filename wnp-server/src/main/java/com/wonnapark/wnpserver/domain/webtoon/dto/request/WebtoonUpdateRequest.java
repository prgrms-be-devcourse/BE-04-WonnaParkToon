package com.wonnapark.wnpserver.domain.webtoon.dto.request;

public record WebtoonUpdateRequest (
        String title,
        String artist,
        String detail,
        String genre,
        String thumbnail
){
}


