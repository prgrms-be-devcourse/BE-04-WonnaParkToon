package com.wonnapark.wnpserver.domain.oauth.token;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = JwtTokenGenerator.class)
class JwtTokenGeneratorTest {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    @Test
    void test() {
        long now = (new Date()).getTime();
        String generate = jwtTokenGenerator.generate("2", new Date(now + ACCESS_TOKEN_EXPIRE_TIME));

        String s = jwtTokenGenerator.extractSubject(generate);

        System.out.println("generate = " + generate);
        System.out.println("s = " + s);
    }
}
