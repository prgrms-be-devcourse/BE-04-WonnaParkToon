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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
class EpisodeRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;
    private Webtoon webtoon;

    @BeforeEach
    void init() {
        webtoon = saveWebtoon();
    }

    @Test
    @DisplayName("에피소드 아이디를 통해 에피소드를 단건 조회할 수 있다.")
    void findById() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        // when
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        // then
        assertThat(foundEpisode).isPresent();
    }

    @Test
    @DisplayName("특정 웹툰아이디를 통해 에피소드를 페이지 조회할 수 있다.")
    void findAllByWebtoonId() {
        // given
        List<Episode> episodes = saveEpisodes(webtoon);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Episode> episodePage = episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable);
        // then
        assertThat(episodePage).isNotNull();
        assertThat(episodePage.getContent()).hasSize(Math.min(episodes.size(), pageable.getPageSize()));
        assertThat(episodePage.getContent().get(0).getWebtoon().getId()).isEqualTo(webtoon.getId());
    }

    @Test
    @DisplayName("에피소드 타이틀을 수정할 수 있다.")
    void changeTitle() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        String newTitle = Instancio.create(String.class);
        // when
        episode.changeTitle(newTitle);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("에피소드 작가의 말을 수정할 수 있다.")
    void changeArtistComment() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        String newArtistComment = Instancio.create(String.class);
        // when
        episode.changeArtistComment(newArtistComment);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("에피소드 썸네일을 수정할 수 있다.")
    void changeThumbNail() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        String newThumbNail = Instancio.create(String.class);
        // when
        episode.changeThumbnail(newThumbNail);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("에피소드 공개일을 수정할 수 있다.")
    void changeReleaseDateTime() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        LocalDateTime newReleaseDateTime = Instancio.create(LocalDateTime.class);
        // when
        episode.changeReleaseDateTime(newReleaseDateTime);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("에피소드 url을 전체 수정할 수 있다.")
    void changeEpisodeUrls() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        List<EpisodeUrl> newEpisodeUrls = makeEpisodeUrls();
        // when
        episode.changeEpisodeUrls(newEpisodeUrls);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("해당 title을 가진 에피소드가 있는지 확인할 수 있다.")
    void existsByTitle_True() {
        // given
        Episode episode = saveOneEpisode(webtoon);
        // when
        boolean result = episodeRepository.existsByTitle(episode.getTitle());
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("해당 title을 가진 에피소드가 있는지 확인할 수 있다.")
    void existsByTitle_False() {
        // given
        // when
        boolean result = episodeRepository.existsByTitle(Instancio.create(String.class));
        // then
        assertThat(result).isFalse();
    }

    private Webtoon saveWebtoon() {
        Webtoon webtoon = Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getId))
                .create();
        return webtoonRepository.save(webtoon);
    }

    private Episode saveOneEpisode(Webtoon webtoon) {
        Episode episode = Instancio.of(Episode.class)
                .ignore(field(Episode::getId))
                .ignore(field(Episode::getWebtoon))
                .ignore(field(Episode::getEpisodeUrls))
                .ignore(field(Episode::isDeleted))
                .create();
        episode.setWebtoon(webtoon);
        episode.setEpisodeUrls(makeEpisodeUrls());
        return episodeRepository.save(episode);
    }

    private List<Episode> saveEpisodes(Webtoon webtoon) {
        List<Episode> episodes = Instancio.ofList(Episode.class)
                .ignore(field(Episode::getId))
                .ignore(field(Episode::getWebtoon))
                .ignore(field(Episode::getEpisodeUrls))
                .ignore(field(Episode::isDeleted))
                .create();
        episodes.forEach(e -> e.setWebtoon(webtoon));
        episodes.forEach(e -> e.setEpisodeUrls(makeEpisodeUrls()));
        return episodeRepository.saveAll(episodes);
    }

    private List<EpisodeUrl> makeEpisodeUrls() {
        return Instancio.ofList(EpisodeUrl.class)
                .ignore(field(EpisodeUrl::getId))
                .ignore(field(EpisodeUrl::getEpisode))
                .create();
    }

}
