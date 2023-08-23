package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
class EpisodeUrlRepositoryTest {

    @Autowired
    private EpisodeUrlRepository episodeUrlRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;
    private EpisodeUrl episodeUrl;

    @BeforeEach
    void init() {
        Webtoon webtoon = Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getId))
                .create();
        webtoonRepository.save(webtoon);

        episodeUrl = Instancio.of(EpisodeUrl.class)
                .ignore(field(EpisodeUrl::getId))
                .ignore(field(EpisodeUrl::getEpisode))
                .generate(field(EpisodeUrl::getOrder), gen -> gen.ints().range(1, 200))
                .create();
        Episode episode = Instancio.of(Episode.class)
                .ignore(field(Episode::getId))
                .ignore(field(Episode::getEpisodeUrls))
                .ignore(field(Episode::getWebtoon))
                .create();
        episode.setWebtoon(webtoon);
        episode.setEpisodeUrls(List.of(episodeUrl));

        episodeRepository.save(episode);
    }

    @Test
    @DisplayName("에피소드 URL을 조회 할 수 있다.")
    void testFindById() {
        // given
        // when
        Optional<EpisodeUrl> foundEpisodeUrl = episodeUrlRepository.findById(episodeUrl.getId());
        // then
        assertThat(foundEpisodeUrl).isPresent();
    }

}
