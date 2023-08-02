package org.standard.dreamcalendar.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.user.type.Role;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String email;
    private transient String password;
    private String name;
    private String picture;
    private Role role;

    @JsonProperty("refresh_token")
    private String refreshToken;

}
