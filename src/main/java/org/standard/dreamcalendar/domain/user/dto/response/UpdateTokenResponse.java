package org.standard.dreamcalendar.domain.user.dto.response;

public class UpdateTokenResponse extends TokenResponse{
    public UpdateTokenResponse(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }
}
