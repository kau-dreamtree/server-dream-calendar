package org.standard.dreamcalendar.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> logInByEmailPassword(@RequestBody UserDto user) throws Exception {
        System.out.println("## controller reached");
        TokenResponse response = authService.logInByEmailPassword(user);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/auth")
    public ResponseEntity<HttpStatus> authorize(@AccessToken TokenValidationResult result) {
        return ResponseEntity.status(authService.authorize(result)).build();
    }

    @GetMapping("/auth-refresh")
    public ResponseEntity<TokenResponse> updateToken(@RequestHeader("Authorization") String refreshToken) throws Exception {
        TokenResponse response = authService.updateToken(refreshToken.split("Bearer ")[1]);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<HttpStatus> logOut(@AccessToken TokenValidationResult result) {
        return ResponseEntity.status(authService.logOut(result)).build();
    }
}
