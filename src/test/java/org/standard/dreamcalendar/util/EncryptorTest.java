package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptorTest {

    UtilTestConfig config;
    Encryptor encryptor;

    @BeforeEach
    void setUp() {
        config = new UtilTestConfig();
        encryptor = config.encryptor();
    }

    @Test
    void sha256() throws NoSuchAlgorithmException {

        // given
        String plainText = "1234";
        String expectedHash = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"; // A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=

        // when
        String actualHash = encryptor.SHA256(plainText);

        // then
        assertEquals(expectedHash, actualHash);
    }

    @Test
    void aes256Encode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        // given
        String plainText = "test";
        String expected = "2611EE5692871BA70191015E67C66E38"; // JhHuVpKHG6cBkQFeZ8ZuOA==

        // when
        String actual = encryptor.AES256Encode(plainText);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void aes256Decode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        // given
        String cipherText = "2611EE5692871BA70191015E67C66E38";
        String expected = "test";

        // when
        String actual = encryptor.AES256Decode(cipherText);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void bytesToHex() {

        // given
        byte[] bytes = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        String expected = "1234ABCDEF";

        // when
        String actual = encryptor.bytesToHex(bytes);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void hexToBytes() {

        // given
        String hex = "1234ABCDEF";
        byte[] expected = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

        // when
        byte[] actual = encryptor.hexToBytes(hex);

        // then
        assertArrayEquals(expected, actual);
    }

}
