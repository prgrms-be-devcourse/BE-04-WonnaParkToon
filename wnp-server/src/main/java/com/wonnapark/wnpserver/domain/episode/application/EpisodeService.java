package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EpisodeService {

    Long createEpisode(Long webtoonId, EpisodeCreationRequest request);

    Page<EpisodeListFormResponse> findEpisodeListForm(Long webtoonId, Pageable pageable);

    EpisodeDetailFormResponse findEpisodeMainForm(Long id);

    void updateEpisodeTitle(Long id, EpisodeTitleUpdateRequest request);

    void updateEpisodeArtistComment(Long id, EpisodeArtistCommentUpdateRequest request);

    void updateEpisodeThumbnail(Long id, EpisodeThumbnailUpdateRequest request);

    void updateEpisodeReleaseDateTime(Long id, EpisodeReleaseDateTimeUpdateRequest request);

    void deleteEpisode(Long id);
}
