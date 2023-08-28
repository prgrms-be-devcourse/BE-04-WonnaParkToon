package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    boolean existsByEpisodeIdAndUserId(Long episodeId, Long userId);

    @Query("SELECT v.episode.id FROM ViewHistory v WHERE v.webtoon.id = :webtoonId AND v.user.id = :userId")
    List<Long> findEpisodeIdByWebtoonIdAndUserId(Long webtoonId, Long userId);

}
