package org.standard.dreamcalendar.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.token.AccessTokenProvider;
import org.standard.dreamcalendar.global.util.token.RefreshTokenProvider;

@TestConfiguration
public class UtilTestConfig {

    protected static final String TEST_KEY_256 = "l6ybz1s9hk6j0g1qmzotgoi5w2ym1nrm"; // 32bytes(256bit)

    @Bean
    AccessTokenProvider accessTokenProvider() {
        return new AccessTokenProvider(TEST_KEY_256, 1L);
    }

    @Bean
    RefreshTokenProvider refreshTokenProvider() {
        return new RefreshTokenProvider();
    }
}
