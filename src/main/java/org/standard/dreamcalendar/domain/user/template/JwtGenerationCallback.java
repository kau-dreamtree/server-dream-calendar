package org.standard.dreamcalendar.domain.user.template;

import org.standard.dreamcalendar.domain.user.User;

import java.security.NoSuchAlgorithmException;

public interface JwtGenerationCallback {
    boolean validateUser(User userDto) throws NoSuchAlgorithmException;
}
