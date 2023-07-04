package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.Test;
import org.standard.dreamcalendar.domain.user.type.TokenType;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    String accessKey = "test-test-test-test-test-test-test";
    String refreshKey = "test-test-test-test-test-test-test";
    long accessExpire = 1L;
    long refreshExpire = 1L;

    JwtProvider jwtProvider = new JwtProvider(accessKey, refreshKey, accessExpire, refreshExpire);

    @Test
    void generate() {




    }

    @Test
    void validateToken() {
    }

    @Test
    void extractId() {

        Long id = 1L;
        TokenType type = TokenType.AccessToken;

        String token = jwtProvider.generate(id, type);

        Long result = jwtProvider.extractId(token, TokenType.AccessToken);

        assertEquals(id, result);

    }

}