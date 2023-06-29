package org.standard.dreamcalendar.util;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptorTest {

    @Test
    public void testSHA256() throws NoSuchAlgorithmException {
        Encryptor encryptor = new Encryptor();
        String inputText = "test";
        String expectedHash = "n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=";

        // SHA-256 암호화 메소드를 직접 호출하여 예상 결과와 실제 결과를 비교
        String actualHash = encryptor.SHA256(inputText);
        assertEquals(expectedHash, actualHash);
    }

}
