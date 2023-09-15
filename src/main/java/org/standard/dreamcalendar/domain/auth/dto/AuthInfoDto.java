package org.standard.dreamcalendar.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthInfoDto {
    Long id;
    @JsonProperty("refresh_token") String refreshToken;
}
