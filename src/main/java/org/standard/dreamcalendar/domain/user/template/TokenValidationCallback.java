package org.standard.dreamcalendar.domain.user.template;

import org.standard.dreamcalendar.domain.user.User;

import java.security.NoSuchAlgorithmException;

public interface TokenValidationCallback {
    boolean validate(User userDto) throws NoSuchAlgorithmException;
}
