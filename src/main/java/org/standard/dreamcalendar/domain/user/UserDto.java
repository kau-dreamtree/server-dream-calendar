package org.standard.dreamcalendar.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String email;
    private String password;
    private String username;
    private String accessToken;
    private String refreshToken;

}
