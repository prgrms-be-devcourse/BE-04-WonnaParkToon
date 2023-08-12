package com.wonnapark.wnpserver.domain.user;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private OauthProvider platform;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private String age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @Builder
    private User(OauthProvider platform, String nickname, String email, String age, String gender, boolean isDeleted) {
        this.platform = platform;
        this.nickname = nickname;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.isDeleted = isDeleted;
    }
}
