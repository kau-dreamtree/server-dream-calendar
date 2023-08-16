package org.standard.dreamcalendar.global.util.token;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RefreshTokenProvider implements TokenProvider {

    @Override
    public String generate(Long id) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generate(Long id, String timeUnit, Long duration) {
        return generate(id);
    }
}
