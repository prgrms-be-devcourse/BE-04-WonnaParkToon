package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.ViewCoolTime;
import com.wonnapark.wnpserver.episode.infrastructure.ViewCoolTimeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wonnapark.wnpserver.episode.EpisodeFixtures.ip;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.viewCoolTimeRedisKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EpisodeViewServiceTest {

    @InjectMocks
    private EpisodeViewService episodeViewService;
    @Mock
    private ViewCoolTimeRepository viewCoolTimeRepository;
    @Mock
    private ViewHistoryService viewHistoryService;

    @Test
    @DisplayName("회원의 레디스에 해당 키가 있으면 조회 정보를 저장하지 않을 수 있다")
    void user_saveViewInfo_true() {
        // given
        Long userId = 1L;
        Long episodeId = 1L;
        String key = viewCoolTimeRedisKey(userId, episodeId);

        given(viewCoolTimeRepository.existsById(key)).willReturn(true);

        // when
        boolean result = episodeViewService.saveViewInfo(userId, episodeId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원의 레디스에 조회 정보를 저장할 수 있다")
    void user_saveViewInfo_false() {
        // given
        Long userId = 1L;
        Long episodeId = 1L;
        String key = viewCoolTimeRedisKey(userId, episodeId);

        given(viewCoolTimeRepository.existsById(key)).willReturn(false);

        // when
        boolean result = episodeViewService.saveViewInfo(userId, episodeId);

        // then
        assertThat(result).isTrue();
        then(viewHistoryService).should(atMostOnce()).saveViewHistory(userId, episodeId);
        then(viewCoolTimeRepository).should(atMostOnce()).save(new ViewCoolTime(key, any()));
    }

    @Test
    @DisplayName("비회원의 조회 정보를 저장할 수 있다")
    void guest_saveViewInfo_false() {
        // Arrange
        String ip = "127.0.0.1";
        Long episodeId = 1L;
        String key = viewCoolTimeRedisKey(ip, episodeId);

        given(viewCoolTimeRepository.existsById(key)).willReturn(false);

        // Act
        boolean result = episodeViewService.saveViewInfo(ip, episodeId);

        // Assert
        assertThat(result).isTrue();
        then(viewCoolTimeRepository).should(atMostOnce()).save(any(ViewCoolTime.class));
    }

    @Test
    void guest_saveViewInfo_true() {
        // Arrange
        String ip = ip();
        Long episodeId = 1L;
        String key = viewCoolTimeRedisKey(ip, episodeId);

        given(viewCoolTimeRepository.existsById(key)).willReturn(true);

        // Act
        boolean result = episodeViewService.saveViewInfo(ip, episodeId);

        // Assert
        assertThat(result).isFalse();
        then(viewCoolTimeRepository).should(never()).save(any(ViewCoolTime.class));
    }
}
