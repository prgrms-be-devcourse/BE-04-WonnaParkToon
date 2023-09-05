package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.DUPLICATED_EPISODE_TITLE;
import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.EPISODE_NOT_FOUND;
import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.WEBTOON_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
public class EpisodeManageService implements EpisodeManageUseCase {

    private final EpisodeRepository episodeRepository;
    private final WebtoonRepository webtoonRepository;

    @Override
    public Long createEpisode(Long webtoonId, EpisodeCreationRequest request) {
        if (episodeRepository.existsByWebtoonIdAndTitle(webtoonId, request.title())) {
            throw new EntityExistsException(DUPLICATED_EPISODE_TITLE.getMessage(webtoonId, request.title()));
        }
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(WEBTOON_NOT_FOUND.getMessage(webtoonId)));
        Episode newEpisode = request.toEntity();
        newEpisode.setWebtoon(webtoon);
        return episodeRepository.save(newEpisode).getId();
    }

    @Override
    public void updateEpisodeTitle(Long episodeId, EpisodeTitleUpdateRequest request) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.changeTitle(request.title());
    }

    @Override
    public void updateEpisodeArtistComment(Long episodeId, EpisodeArtistCommentUpdateRequest request) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.changeArtistComment(request.artistComment());
    }

    @Override
    public void updateEpisodeThumbnail(Long episodeId, EpisodeThumbnailUpdateRequest request) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.changeThumbnail(request.thumbnail());
    }

    @Override
    public void updateEpisodeReleaseDateTime(Long episodeId, EpisodeReleaseDateTimeUpdateRequest request) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.changeReleaseDateTime(request.releaseDateTime());
    }

    @Override
    public void updateEpisodeUrls(Long episodeId, EpisodeUrlsUpdateRequest request) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.changeEpisodeUrls(request.toEntityList());
    }

    @Override
    public void deleteEpisode(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
        episode.delete();
    }

}
