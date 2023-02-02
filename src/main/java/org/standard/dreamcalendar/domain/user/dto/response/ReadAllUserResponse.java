package org.standard.dreamcalendar.domain.user.dto.response;

import lombok.Data;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import java.util.List;

@Data
public class ReadAllUserResponse {

     private String message;

     private List<UserDto> userDtoList;

     public ReadAllUserResponse(String message) {
          this.message = message;
     }

     public ReadAllUserResponse(String message, List<UserDto> userDtoList) {
          this.message = message;
          this.userDtoList = userDtoList;
     }

}
