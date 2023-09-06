package com.wonnapark.wnpserver.global.auth;

public final class AuthenticationContextHolder {

    private AuthenticationContextHolder() {
    }

    private static final ThreadLocal<Authentication> authenticationHolder =
            ThreadLocal.withInitial(Authentication::initAuthentication);

    public static void clearContext() {
        authenticationHolder.remove();
    }

    public static Authentication getAuthentication() {
        return authenticationHolder.get();
    }

    public static void setAuthenticationHolder(Authentication authentication) {
        authenticationHolder.set(authentication);
    }

}
