package com.wonnapark.wnpserver.auth.presentation;

import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.user.Role;
import org.instancio.Instancio;

public class AuthFixtures {

    public static AuthTokenResponse createAuthTokenResponse() {
        return Instancio.create(AuthTokenResponse.class);
    }

    public static Authentication createUserAuthentication() {
        return new Authentication(1L, 18, Role.USER);
    }
}
