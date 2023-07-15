package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;

class JwtProviderTest {

    UtilTestConfig config;
    JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        config = new UtilTestConfig();
        jwtProvider = config.jwtProvider();
    }

    @Test
    void validToken() {

        // given
        long userId = 1L;
        TokenType type = TokenType.AccessToken;

        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.VALID, userId);

        // when
        String token = jwtProvider.generate(userId, type);
        TokenValidationResult result = jwtProvider.validateToken(token, type);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void expiredToken() {

        // given
        long userId = 1L;
        TokenType type = TokenType.AccessToken;
        String timeUnit = "millis";

        long duration = 0L;     // duration 바꿔가면서 test

        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.EXPIRED, null);

        // when
        String token = jwtProvider.generate(userId, type, timeUnit, duration);
        TokenValidationResult result = jwtProvider.validateToken(token, type);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void invalidToken() {

        // given
        TokenType type = TokenType.AccessToken;

        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.INVALID, null);

        // when
        String token = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";
        TokenValidationResult result = jwtProvider.validateToken(token, type);

        // then
        Assertions.assertEquals(expected, result);
    }

}