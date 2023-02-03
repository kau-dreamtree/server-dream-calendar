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
@RestController("/admin")
public class UserAdminController {

    private final UserService userService;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/users")
    public ResponseEntity<ReadAllUserResponse> readAllUsers(@RequestHeader("Authorization") String authorization) {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<UserDto> userDtoList = userService.findAll();

        String message = (userDtoList.isEmpty()) ? "등록된 사용자가 없습니다." : "success";

        return ResponseEntity.status(HttpStatus.OK).body(new ReadAllUserResponse(message, userDtoList));
    }
}
