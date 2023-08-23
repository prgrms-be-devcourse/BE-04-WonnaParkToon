package com.wonnapark.wnpserver.domain.episode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "episode_urls")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "orders", nullable = false, columnDefinition = "SMALLINT")
    private int order;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    private EpisodeUrl(String url, int order) {
        this.url = url;
        this.order = order;
    }

    public void changeUrl(String url) {
        // TODO: 업데이트 고민하고 상의할 부분이 많음
        this.url = url;
    }

    public void delete() {
        isDeleted = true;
        episode.getEpisodeUrls().remove(this);
    }

    public void setEpisode(Episode episode) {
        episode.getEpisodeUrls().add(this);
        this.episode = episode;
    }

}
