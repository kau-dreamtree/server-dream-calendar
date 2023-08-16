package org.standard.dreamcalendar.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Encryptor {

    private static String key;

    public static String SHA256(String plainText) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(plainText.getBytes(StandardCharsets.UTF_8));
        return Encoder.bytesToHex(messageDigest.digest());
    }

    public static String AES256Encode(String plainText) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(key.substring(0, 16).getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Encoder.bytesToHex(cipherText);
    }

    public static String AES256Decode(String cipherText) throws Exception {

        byte[] cipherBytes = Encoder.hexToBytes(cipherText);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(key.substring(0, 16).getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] plainTexts = cipher.doFinal(cipherBytes);

        return new String(plainTexts, StandardCharsets.UTF_8);
    }

    @Value("${aes-key}")
    public void setKey(String key) {
        Encryptor.key = key;
    }
}
