package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
class EpisodeRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;
    private Episode episode;

    @BeforeEach
    void init() {
        episode = episodeRepository.save(
                Instancio.of(Episode.class)
                        .ignore(field(Episode::getId))
                        .create()
        );
    }

    @Test
    @DisplayName("에피소드를 단건 조회할 수 있다.")
    void testFindById() {
        // given
        // when
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        // then
        assertThat(foundEpisode).isPresent();
    }

    @Test
    @DisplayName("에피소드를 페이지 조회할 수 있다.")
    void testFindPageable() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Episode> episodePage = episodeRepository.findAll(pageable);
        // then
        assertThat(episodePage).isNotNull();
    }

    @Test
    @DisplayName("에피소드 타이틀을 수정할 수 있다.")
    void testUpdateTitle() {
        // given
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
    void testUpdateArtistComment() {
        // given
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
    void testUpdateThumbNail() {
        // given
        String newThumbNail = Instancio.create(String.class);
        // when
        episode.changeThumbNail(newThumbNail);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

    @Test
    @DisplayName("에피소드 공개일을 수정할 수 있다.")
    void testUpdateReleaseDateTime() {
        // given
        LocalDateTime newReleaseDateTime = Instancio.create(LocalDateTime.class);
        // when
        episode.changeReleaseDateTime(newReleaseDateTime);
        // then
        Optional<Episode> foundEpisode = episodeRepository.findById(episode.getId());
        assertThat(foundEpisode).isPresent();
        assertThat(foundEpisode.get()).usingRecursiveComparison().isEqualTo(episode);
    }

}
