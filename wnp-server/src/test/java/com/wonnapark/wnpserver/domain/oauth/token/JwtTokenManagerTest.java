package com.wonnapark.wnpserver.domain.oauth.token;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = JwtTokenManager.class)
class JwtTokenManagerTest {

    @Autowired
    private JwtTokenManager jwtTokenManager;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    @Test
    void test() {
        long now = (new Date()).getTime();
        String generate = jwtTokenManager.generate("2", new Date(now - ACCESS_TOKEN_EXPIRE_TIME));

        String s = jwtTokenManager.extractSubject(generate);

        System.out.println("generate = " + generate);
        System.out.println("s = " + s);
    }
}
