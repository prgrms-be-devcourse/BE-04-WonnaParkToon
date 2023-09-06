package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeManageUseCase;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeCreationResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/admin/episode")
@RequiredArgsConstructor
public class AdminEpisodeController {

    private final EpisodeManageUseCase episodeManageUseCase;

    @PostMapping
    @ResponseStatus(CREATED)
    public ApiResponse<EpisodeCreationResponse> createEpisode(
            @RequestParam Long webtoonId,
            @RequestBody @Valid EpisodeCreationRequest episodeCreationRequest,
            HttpServletResponse response
    ) {
        Long episodeId = episodeManageUseCase.createEpisode(webtoonId, episodeCreationRequest);

        String uri = URI.create(String.format("/api/v1/common/episode/detail/%d", episodeId)).toString();
        response.setHeader("Location", uri);

        return ApiResponse.from(new EpisodeCreationResponse(episodeId));
    }

    @PatchMapping("{id}/title")
    @ResponseStatus(OK)
    public void updateEpisodeTitle(@PathVariable Long id, @RequestBody @Valid EpisodeTitleUpdateRequest request) {
        episodeManageUseCase.updateEpisodeTitle(id, request);
    }

    @PatchMapping("/{id}/artist-comment")
    @ResponseStatus(OK)
    public void updateEpisodeArtistComment(@PathVariable Long id, @RequestBody @Valid EpisodeArtistCommentUpdateRequest request) {
        episodeManageUseCase.updateEpisodeArtistComment(id, request);
    }

    @PatchMapping("/{id}/thumbnail")
    @ResponseStatus(OK)
    public void updateEpisodeThumbnail(@PathVariable Long id, @RequestBody @Valid EpisodeThumbnailUpdateRequest request) {
        episodeManageUseCase.updateEpisodeThumbnail(id, request);
    }

    @PatchMapping("/{id}/release-datetime")
    @ResponseStatus(OK)
    public void updateEpisodeReleaseDateTime(@PathVariable Long id, @RequestBody @Valid EpisodeReleaseDateTimeUpdateRequest request) {
        episodeManageUseCase.updateEpisodeReleaseDateTime(id, request);
    }

    @PatchMapping("/{id}/image-urls")
    @ResponseStatus(OK)
    public void updateEpisodeUrls(@PathVariable Long id, @RequestBody @Valid EpisodeUrlsUpdateRequest request) {
        episodeManageUseCase.updateEpisodeUrls(id, request);
    }

}
