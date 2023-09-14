package org.standard.dreamcalendar.domain.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ActiveProfiles;
import org.standard.dreamcalendar.domain.auth.AuthInfo;
import org.standard.dreamcalendar.domain.auth.AuthRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataRedisTest
class RedisTest {

    @Autowired
    AuthRepository authRepository;

    @BeforeEach
    void clear() {
        authRepository.deleteAll();
    }

    @Test
    void save() {

        // given
        Long userId = 1L;
        String refreshToken = "refreshToken";
        Long ttl = 3600L;

        AuthInfo authInfo = AuthInfo.builder().userId(userId).refreshToken(refreshToken).expireMilli(ttl).build();

        // when
        authRepository.save(authInfo);


        // then
        AuthInfo result = authRepository.findByRefreshToken(refreshToken).get();

        assertEquals(userId, result.getId());
        assertEquals(refreshToken, result.getRefreshToken());
        assertEquals(ttl, result.getTtl());
    }
}
