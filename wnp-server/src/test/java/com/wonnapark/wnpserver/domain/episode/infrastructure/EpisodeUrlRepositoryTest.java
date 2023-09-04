package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
class EpisodeUrlRepositoryTest {

    @Autowired
    private EpisodeUrlRepository episodeUrlRepository;
    private EpisodeUrl episodeUrl;

    @BeforeEach
    void init() {
        episodeUrl = episodeUrlRepository.save(
                Instancio.of(EpisodeUrl.class)
                        .ignore(field(EpisodeUrl::getId))
                        .create()
        );
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
