package org.standard.dreamcalendar.config;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class PasswordEncoder {

    public String sha256(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(messageDigest.digest());
    }

}
