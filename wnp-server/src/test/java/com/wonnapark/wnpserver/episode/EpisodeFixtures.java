package com.wonnapark.wnpserver.episode;

import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.instancio.Instancio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.instancio.Assign.valueOf;
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
                .set(field(Episode::getWebtoon), webtoon)
                .ignore(field(Episode::isDeleted))
                .ignore(field(Episode::getEpisodeUrls))
                .create();
        episode.setEpisodeUrls(createEpisodeUrls(1L));
        return episode;
    }

    public static List<Episode> createEpisodes(Webtoon webtoon) {

        List<Episode> episodes = Instancio.ofList(Episode.class)
                .set(field(Episode::getWebtoon), webtoon)
                .assign(valueOf(Episode::getId).generate(gen -> gen.longSeq().start(1L)))
                .ignore(field(Episode::isDeleted))
                .ignore(field(Episode::getEpisodeUrls))
                .create();

        AtomicReference<Long> episodeUrlsId = new AtomicReference<>(1L);
        episodes.forEach(episode -> {
            episode.setEpisodeUrls(createEpisodeUrls(episodeUrlsId.get()));
            episodeUrlsId.updateAndGet(v -> v + episode.getEpisodeUrls().size());
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

    public static List<EpisodeUrl> createEpisodeUrls(Long initialEpisodeUrl) {
        return Instancio.ofList(EpisodeUrl.class)
                .assign(valueOf(EpisodeUrl::getId).generate(gen -> gen.longSeq().start(initialEpisodeUrl)))
                .ignore(field(EpisodeUrl::getEpisode))
                .create();
    }

    public static Pageable createPageable() {
        return new WebtoonListPageRequest(0, Sort.Direction.DESC).toPageable();
    }

}
