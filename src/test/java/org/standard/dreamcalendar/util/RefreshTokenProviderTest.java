package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.Test;
import org.standard.dreamcalendar.global.util.token.RefreshTokenProvider;

class RefreshTokenProviderTest {

    RefreshTokenProvider tokenProvider = new RefreshTokenProvider();

    @Test
    void getRefreshToken() {
        long id = 1L;
        System.out.println(tokenProvider.generate(id));
    }
}
