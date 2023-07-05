package org.standard.dreamcalendar.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.util.JwtProvider;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccessToken.class);
    }

    @Override
    public TokenValidationResult resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = httpServletRequest.getHeader("Authorization").split("Bearer ")[1];

        return jwtProvider.validateToken(token, TokenType.AccessToken);
    }
}
