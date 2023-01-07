package org.statndard.dreamcalendar.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.standard.dreamcalendar.config.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void 토큰_생성_테스트() {



//        assertThat(givenDecode).isEqualTo(expected);
    }

}
