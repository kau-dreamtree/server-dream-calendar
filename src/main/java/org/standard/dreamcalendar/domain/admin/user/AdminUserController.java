package org.standard.dreamcalendar.domain.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.AdminReadAllUserResponse;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final AdminAuthService adminAuthService;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/admin/users")
    public ResponseEntity<AdminReadAllUserResponse> readAllUsers(@RequestHeader("Authorization") String authorization) {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.badRequest().build();
        }

        List<UserDto> userDtoList = adminAuthService.findAll();

        String message = (userDtoList.isEmpty()) ? "등록된 사용자가 없습니다." : "success";

        return ResponseEntity.status(HttpStatus.OK).body(new AdminReadAllUserResponse(message, userDtoList));
    }

    // logInByEmailPassword 수정
    @PostMapping("/admin/auth")
    public ResponseEntity<TokenResponse> tokenExpirationTest(
            @RequestHeader("Authorization") String authorization, @RequestBody AdminTokenExpirationTestDto dto
    ) throws Exception {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.badRequest().build();
        }

        TokenResponse response = adminAuthService.tokenExpirationTest(dto);

        return (response != null) ?
                ResponseEntity.ok(response) :
                ResponseEntity.notFound().build();
    }

}
