package org.standard.dreamcalendar.domain.user.dto.response;

public class LogInByEmailPasswordResponse extends TokenResponse {
    public LogInByEmailPasswordResponse(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }
}
