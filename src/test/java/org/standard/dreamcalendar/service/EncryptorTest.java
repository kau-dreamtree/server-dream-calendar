package org.standard.dreamcalendar.service;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptorTest {

    @Test
    public void testSHA256() throws NoSuchAlgorithmException {
        Encryptor encryptor = new Encryptor();
        String inputText = "test";
        String expectedHash = "9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08";

        // SHA-256 암호화 메소드를 직접 호출하여 예상 결과와 실제 결과를 비교
        String actualHash = encryptor.SHA256(inputText);
        assertEquals(expectedHash, actualHash);
    }

}
