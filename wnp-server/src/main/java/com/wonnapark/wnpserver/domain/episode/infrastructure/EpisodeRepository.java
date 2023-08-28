package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    Page<Episode> findAllByWebtoonId(Long webtoonId, Pageable pageable);

    boolean existsByTitle(String title);

}
