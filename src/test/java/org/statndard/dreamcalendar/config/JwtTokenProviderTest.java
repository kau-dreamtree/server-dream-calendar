package org.statndard.dreamcalendar.config;

import org.junit.Test;

import static org.standard.dreamcalendar.config.JwtTokenProvider.EXPIRATION_MS;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    @Test
    public void timeUnitTest() {
        System.out.println(EXPIRATION_MS);
        assertThat(EXPIRATION_MS).isEqualTo(7200000L);
    }

}
