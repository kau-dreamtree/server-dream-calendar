package org.standard.dreamcalendar.domain.admin.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AdminTokenExpirationTestDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String picture;

    @JsonProperty("time_unit")
    private String timeUnit;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_expiration")
    private Long accessExpiration;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
