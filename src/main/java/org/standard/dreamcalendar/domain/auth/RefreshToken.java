package org.standard.dreamcalendar.domain.auth;

import org.springframework.data.annotation.Id;

public class RefreshToken {

    @Id
    private String refreshToken;
    private Long userId;

    public RefreshToken(final String refreshToken, final Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }
}
