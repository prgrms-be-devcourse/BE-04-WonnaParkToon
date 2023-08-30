package com.wonnapark.wnpserver.domain.episode;

import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import org.instancio.Instancio;

import java.util.List;

import static org.instancio.Select.field;

public final class EpisodeFixtures {
    private EpisodeFixtures() {
    }

    public static Webtoon createWebtoon() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getIsDeleted))
                .create();
    }

    public static User createUser() {
        return Instancio.of(User.class)
                .ignore(field(User::getIsDeleted))
                .create();
    }

    public static Episode createEpisode(Webtoon webtoon) {
        Episode episode = Instancio.of(Episode.class)
                .ignore(field(Episode::getWebtoon))
                .ignore(field(Episode::isDeleted))
                .ignore(field(Episode::getEpisodeUrls))
                .create();

        episode.setWebtoon(webtoon);

        List<EpisodeUrl> episodeUrls = createEpisodeUrls();
        episode.setEpisodeUrls(episodeUrls);
        return episode;
    }

    public static List<Episode> createEpisodes(Webtoon webtoon) {
        List<Episode> episodes = Instancio.ofList(Episode.class)
                .ignore(field(Episode::getWebtoon))
                .ignore(field(Episode::isDeleted))
                .ignore(field(Episode::getEpisodeUrls))
                .create();

        episodes.forEach(episode -> {
            episode.setWebtoon(webtoon);

            List<EpisodeUrl> episodeUrls = createEpisodeUrls();
            episode.setEpisodeUrls(episodeUrls);
        });
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
                .ignore(field(EpisodeUrl::getEpisode))
                .create();
    }

}
