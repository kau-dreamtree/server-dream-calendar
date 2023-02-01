package org.standard.dreamcalendar.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReadAllUserResponse {

     private String message;

     private List<UserDto> userDtoList;


}
