package org.statndard.dreamcalendar.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.standard.dreamcalendar.DreamCalendarApplication;
import org.standard.dreamcalendar.service.Encryptor;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Encryptor 작동 테스트")
@SpringBootTest(classes = DreamCalendarApplication.class)
@RunWith(SpringRunner.class)
public class EncryptorTest {

    @Autowired
    Encryptor encryptor;

    @Test
    public void RSA_암호화_복호화_테스트() throws Exception {

        String given = "Hello World!";
        String expected = "Hello World!";

        String givenEncode = encryptor.AES256Encode(given);
        String givenDecode = encryptor.AES256Decode(givenEncode);

        assertThat(givenDecode).isEqualTo(expected);

    }
}
