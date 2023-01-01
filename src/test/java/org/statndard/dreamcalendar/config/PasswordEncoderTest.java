package org.statndard.dreamcalendar.config;

import org.junit.Test;
import org.standard.dreamcalendar.config.PasswordEncoder;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void sha256test() throws NoSuchAlgorithmException {

        String given = "Jaewoo";

        String expect = "4C9FAA8B025F00AE32414EE1856026249F9BEBB852AF11BB0DD2B51E0BC03190";

        String digest = PasswordEncoder.sha256(given);

        assertThat(digest).isEqualTo(expect);
    }
}
