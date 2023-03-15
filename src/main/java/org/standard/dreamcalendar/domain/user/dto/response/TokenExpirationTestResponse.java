package org.standard.dreamcalendar.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class TokenExpirationTestResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_expiration")
    private Date accessExpiration;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_expiration")
    private Date refreshExpiration;

}
