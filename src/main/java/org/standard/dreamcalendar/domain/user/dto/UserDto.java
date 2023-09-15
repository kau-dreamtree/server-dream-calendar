package org.standard.dreamcalendar.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.user.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String picture;
    private Role role;
}
