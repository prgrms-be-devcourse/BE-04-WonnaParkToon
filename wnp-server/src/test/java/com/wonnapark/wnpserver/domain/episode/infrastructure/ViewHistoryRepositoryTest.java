package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;


@DataJpaTest
class ViewHistoryRepositoryTest {

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    private Webtoon webtoon;
    private User user;
    private List<Episode> episodes;

    @BeforeEach
    void init() {
        webtoon = saveWebtoon();
        user = saveUser();
        episodes = saveEpisodes(webtoon);
    }

    @Test
    @DisplayName("웹툰 ID와 유저 ID를 통해 view_history 테이블에서 에피소드 ID를 찾을 수 있다")
    void name() {
        // given
        saveViewHistories(user, webtoon, episodes);
        // when
        List<Long> episodeIdByWebtoonIdAndUserId = viewHistoryRepository.findEpisodeIdByWebtoonIdAndUserId(webtoon.getId(), user.getId());
        // then
        assertThat(episodeIdByWebtoonIdAndUserId).hasSize(episodes.size());
        assertThat(episodeIdByWebtoonIdAndUserId).usingRecursiveComparison().isEqualTo(episodes.stream().map(Episode::getId).toList());
    }

    private Webtoon saveWebtoon() {
        Webtoon webtoon = Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getId))
                .create();
        return webtoonRepository.save(webtoon);
    }

    private User saveUser() {
        User user = Instancio.of(User.class)
                .ignore(field(User::getId))
                .create();
        return userRepository.save(user);
    }

    private List<Episode> saveEpisodes(Webtoon webtoon) {
        List<Episode> episodes = Instancio.ofList(Episode.class)
                .ignore(field(Episode::getId))
                .ignore(field(Episode::getWebtoon))
                .ignore(field(Episode::getEpisodeUrls))
                .create();
        episodes.forEach(episode -> {
            episode.setWebtoon(webtoon);
            List<EpisodeUrl> episodeUrls = Instancio.ofList(EpisodeUrl.class)
                    .ignore(field(EpisodeUrl::getEpisode))
                    .ignore(field(EpisodeUrl::getId))
                    .create();
            episode.setEpisodeUrls(episodeUrls);
        });
        return episodeRepository.saveAll(episodes);
    }

    private void saveViewHistories(User user, Webtoon webtoon, List<Episode> episodes) {
        List<ViewHistory> viewHistories = episodes.stream().map(
                episode -> ViewHistory.builder()
                        .webtoon(webtoon)
                        .user(user)
                        .episode(episode)
                        .build()
        ).toList();
        viewHistoryRepository.saveAll(viewHistories);
    }

}
