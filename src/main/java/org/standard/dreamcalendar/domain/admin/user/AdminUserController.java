package org.standard.dreamcalendar.domain.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.response.AdminReadAllUserResponse;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final AdminUserService userAdminService;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/admin/users")
    public ResponseEntity<AdminReadAllUserResponse> readAllUsers(@RequestHeader("Authorization") String authorization) {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<UserDto> userDtoList = userAdminService.findAll();

        String message = (userDtoList.isEmpty()) ? "등록된 사용자가 없습니다." : "success";

        return ResponseEntity.status(HttpStatus.OK).body(new AdminReadAllUserResponse(message, userDtoList));
    }

    // logInByEmailPassword 수정
    @PostMapping("/admin/auth")
    public ResponseEntity<TokenResponse> tokenExpirationTest(
            @RequestHeader("Authorization") String authorization, @RequestBody AdminTokenExpirationTestDto dto
    ) throws Exception {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        TokenResponse response = userAdminService.tokenExpirationTest(dto);

        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
