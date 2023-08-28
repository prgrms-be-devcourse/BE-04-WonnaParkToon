package com.wonnapark.wnpserver.domain.episode.application;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EpisodeErrorMessage {

    static final String EPISODE_NOT_FOUND = "해당 ID의 에피소드를 찾을 수 없습니다: %s";
    static final String USER_NOT_FOUND = "해당 ID의 유저를 찾을 수 없습니다: %s";
    static final String WEBTOON_NOT_FOUND = "해당 ID의 웹툰을 찾을 수 없습니다: %s";
    static final String DUPLICATED_EPISODE = "해당 TITLE의 에피소드가 이미 존재합니다. %s";

}
