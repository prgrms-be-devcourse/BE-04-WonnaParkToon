package com.wonnapark.wnpserver.domain.user;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted IS NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private OAuthProvider platform;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private String ageRange;

    @Column(name = "gender")
    private String gender;

    @Column(name = "is_deleted", nullable = true)
    private LocalDateTime isDeleted;

    @Builder
    private User(OAuthProvider platform, String nickname, String email, String ageRange, String gender) {
        this.platform = platform;
        this.nickname = nickname;
        this.email = email;
        this.ageRange = ageRange;
        this.gender = gender;
    }
}
