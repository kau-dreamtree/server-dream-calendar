package org.standard.dreamcalendar.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.user.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String email;
    private String password;
    private String name;
    private String picture;
    private Role role;
    private String accessToken;
    private String refreshToken;

}
