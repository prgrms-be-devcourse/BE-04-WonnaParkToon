package com.wonnapark.wnpserver.episode.infrastructure;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wonnapark.wnpserver.episode.EpisodeFixtures.episode;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.episodes;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.pageable;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.webtoon;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EpisodeRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;

    private Webtoon webtoon;

    @BeforeEach
    void init() {
        webtoon = webtoonRepository.save(webtoon());
    }

    @Test
    @DisplayName("특정 웹툰아이디를 통해 에피소드를 페이지 조회할 수 있다.")
    void findAllByWebtoonId() {
        // given
        Pageable pageable = pageable();
        List<Episode> episodes = episodeRepository.saveAll(episodes(webtoon));

        // when
        Page<Episode> episodePage = episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable);

        // then
        Set<String> originalEpisodeTitles = episodes.stream().map(Episode::getTitle).collect(Collectors.toSet());
        Set<String> returnedEpisodeTitles = episodePage.map(Episode::getTitle).toSet();

        assertThat(episodePage.getContent()).hasSize(Math.min(episodes.size(), pageable.getPageSize()));
        assertThat(originalEpisodeTitles).containsAll(returnedEpisodeTitles);
    }

    @Test
    @DisplayName("해당 title을 가진 에피소드가 있는지 확인할 수 있다.")
    void existsByTitle_True() {
        // given
        Episode episode = episodeRepository.save(episode(webtoon));
        // when
        boolean result = episodeRepository.existsByWebtoonIdAndTitle(webtoon.getId(), episode.getTitle());
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("해당 title을 가진 에피소드가 있는지 확인할 수 있다.")
    void existsByTitle_False() {
        // given
        String fakeTitle = Instancio.create(String.class);
        // when
        boolean result = episodeRepository.existsByWebtoonIdAndTitle(webtoon.getId(), fakeTitle);
        // then
        assertThat(result).isFalse();
    }

}
