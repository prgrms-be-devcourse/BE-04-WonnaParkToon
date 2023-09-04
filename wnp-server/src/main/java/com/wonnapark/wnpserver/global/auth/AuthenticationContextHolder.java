package com.wonnapark.wnpserver.global.auth;

import org.springframework.util.ObjectUtils;

public class AuthenticationContextHolder {

    private static final ThreadLocal<Authentication> authenticationHolder = new ThreadLocal<>();

    public static void clearContext() {
        authenticationHolder.remove();
    }

    public static Authentication getAuthentication() {
        Authentication authentication = authenticationHolder.get();
        if (ObjectUtils.isEmpty(authentication)) {
            authentication = Authentication.initAuthentication();
            authenticationHolder.set(authentication);
        }
        return authentication;
    }

    public static void setAuthenticationHolder(Authentication authentication) {
        authenticationHolder.set(authentication);
    }

}
