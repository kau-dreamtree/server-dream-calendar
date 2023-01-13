package org.standard.dreamcalendar.config.auth.dto;

import lombok.Getter;
import org.standard.dreamcalendar.domain.user.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private final String email;
    private final String name;
    private final String picture;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.picture = user.getPicture();
    }

}
