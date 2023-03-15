package org.standard.dreamcalendar.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import java.util.List;

@Data
public class AdminReadAllUserResponse {

     private String message;

     @JsonProperty("user_list")
     private List<UserDto> userDtoList;

     public AdminReadAllUserResponse(String message, List<UserDto> userDtoList) {
          this.message = message;
          this.userDtoList = userDtoList;
     }

}
