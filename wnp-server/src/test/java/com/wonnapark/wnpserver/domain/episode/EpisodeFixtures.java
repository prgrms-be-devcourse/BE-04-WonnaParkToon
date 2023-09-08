package com.wonnapark.wnpserver.domain.episode;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeUrl;
import com.wonnapark.wnpserver.episode.ViewHistory;
import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.instancio.Instancio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;

public final class EpisodeFixtures {
    private EpisodeFixtures() {
    }

    public static Webtoon createWebtoon() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getId))
                .ignore(field(Webtoon::getIsDeleted))
                .create();
    }

    public static User createUser() {
        return Instancio.of(User.class)
                .ignore(field(User::getIsDeleted))
                .ignore(field(User::getId))
                .create();
    }

    public static Episode createEpisode(Webtoon webtoon) {
        Episode episode = Instancio.of(Episode.class)
                .set(field(Episode::getWebtoon), webtoon)
                .ignore(field(Episode::getId))
                .ignore(field(Episode::isDeleted))
                .ignore(field(Episode::getEpisodeUrls))
                .create();
        episode.setEpisodeUrls(createEpisodeUrls());
        return episode;
    }

    public static List<Episode> createEpisodes(Webtoon webtoon) {
        List<Episode> episodes = new ArrayList<>();
        for (int i = 0; i < Instancio.create(Integer.class); i++) {
            episodes.add(createEpisode(webtoon));
        }
        return episodes;
    }

    public static ViewHistory createViewHistory(Long userId, Long episodeId) {
        return ViewHistory.builder()
                .userId(userId)
                .episodeId(episodeId)
                .build();
    }

    public static List<ViewHistory> createViewHistories(Long userId, List<Long> episodeIds) {
        return episodeIds.stream().map(
                episodeId -> ViewHistory.builder()
                        .userId(userId)
                        .episodeId(episodeId)
                        .build()
        ).toList();
    }

    public static List<EpisodeUrl> createEpisodeUrls() {
        return Instancio.ofList(EpisodeUrl.class)
                .ignore(field(EpisodeUrl::getId))
                .ignore(field(EpisodeUrl::getEpisode))
                .create();
    }

    public static Pageable createPageable() {
        return new WebtoonListPageRequest(0, Sort.Direction.DESC).toPageable();
    }

}
