package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeUrlRepository extends JpaRepository<EpisodeUrl, Long> {
}
