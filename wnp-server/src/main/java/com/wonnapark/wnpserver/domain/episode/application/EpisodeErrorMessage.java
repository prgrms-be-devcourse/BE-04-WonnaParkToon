package com.wonnapark.wnpserver.domain.episode.application;

import lombok.Getter;

@Getter
enum EpisodeErrorMessage {

    EPISODE_NOT_FOUND("해당 ID의 에피소드를 찾을 수 없습니다: %s"),
    WEBTOON_NOT_FOUND("해당 ID의 웹툰을 찾을 수 없습니다: %s"),
    DUPLICATED_EPISODE("해당 TITLE의 에피소드가 이미 존재합니다. %s");

    private final String message;

    EpisodeErrorMessage(String message) {
        this.message = message;
    }

}
