package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class UtilTestConfig {

    private final String key = "testtesttesttesttesttesttesttest"; // 32bytes(256bit)

    public Encryptor encryptor() {
        return new Encryptor(key);
    }

    public JwtProvider jwtProvider() {
        return new JwtProvider(key, key, 1L, 1L);
    }

}
