package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.token.AccessTokenProvider;

import static org.standard.dreamcalendar.util.UtilTestConfig.TEST_KEY_256;

class AccessTokenProviderTest {

    AccessTokenProvider accessTokenProvider = new AccessTokenProvider(TEST_KEY_256, 1L);

    @Test
    void validToken() throws Exception {

        // given
        long userId = 1L;

        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.VALID, userId);

        // when
        String token = accessTokenProvider.generate(userId);
        TokenValidationResult result = accessTokenProvider.validateToken(token);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void expiredToken() throws Exception {

        // given
        long userId = 1L;
        String timeUnit = "millis";

        long duration = 0L;     // duration 바꿔가면서 test

        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.EXPIRED, null);

        // when
        String token = accessTokenProvider.generate(userId, timeUnit, duration);
        TokenValidationResult result = accessTokenProvider.validateToken(token);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void invalidToken() {

        // given
        TokenValidationResult expected = new TokenValidationResult(TokenValidationStatus.INVALID, null);

        // when
        String token = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";
        TokenValidationResult result = accessTokenProvider.validateToken(token);

        // then
        Assertions.assertEquals(expected, result);
    }

}