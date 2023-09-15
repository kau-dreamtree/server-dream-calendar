package org.standard.dreamcalendar.domain.auth.template;

import org.standard.dreamcalendar.domain.user.User;

import java.security.NoSuchAlgorithmException;

public interface TokenValidationCallback {
    boolean validate(User user) throws NoSuchAlgorithmException;
}
