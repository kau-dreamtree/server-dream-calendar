package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.ReadAllUserResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserAdminController {

    private final UserService userService;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/user/all")
    public ResponseEntity<ReadAllUserResponse> readAllUsers(@RequestHeader("Authorization") String authorization) {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<UserDto> userDtoList = userService.findAll();

        if (userDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReadAllUserResponse response = new ReadAllUserResponse("Success", userDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
