package org.standard.dreamcalendar.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilTestConfig {

    private final String key = "testtesttesttesttesttesttesttest"; // 32bytes(256bit)

    @Bean
    public Encryptor encryptor() {
        return new Encryptor(key);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(key, key, 1L, 1L);
    }

}
