package org.standard.dreamcalendar.util;

import org.junit.jupiter.api.Test;
import org.standard.dreamcalendar.global.util.Encoder;
import org.standard.dreamcalendar.global.util.Encryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.standard.dreamcalendar.util.UtilTestConfig.TEST_KEY_256;

class EncryptorTest {

    Encryptor encryptor = new Encryptor(TEST_KEY_256);

    @Test
    void sha256() throws NoSuchAlgorithmException {

        // given
        String plainText = "1234";
        String expectedHash = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4".toUpperCase(); // "A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=" in base64

        // when
        String actualHash = encryptor.SHA256(plainText);

        // then
        assertEquals(expectedHash, actualHash);
    }

    @Test
    void aes256Encode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        // given
        String plainText = "test";
        String expected = "B05CA968C9D53C70673376A2DEB20BF2";

        // when
        String actual = encryptor.AES256Encode(plainText);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void aes256Decode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        // given
        String cipherText = "B05CA968C9D53C70673376A2DEB20BF2";
        String expected = "test";

        // when
        String actual = encryptor.AES256Decode(cipherText);

        // then
        assertEquals(expected, actual);
    }
}
