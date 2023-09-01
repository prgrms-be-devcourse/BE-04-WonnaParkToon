package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.user.exception.UserNotFoundException;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.global.common.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthorizedArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String USER_NOT_FOUND = "%s는 존재하지 않는 유저 ID 입니다. 토큰의 탈취 또는 변조가 의심됩니다.";

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Authorized.class) != null
                && parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        UserInfo userInfo = jwtTokenService.extractUserInfo(accessToken);
        if (userRepository.existsById(userInfo.userId())) {
            return userInfo;
        }
        throw new UserNotFoundException(String.format(USER_NOT_FOUND, userInfo.userId()));
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        return token;
    }
}
