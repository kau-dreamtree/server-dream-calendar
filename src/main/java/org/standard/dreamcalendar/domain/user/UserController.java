package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.auth.AccessToken;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> signUp(@RequestBody UserDto user) throws NoSuchAlgorithmException {
        return (userService.findByEmail(user.getEmail()).isPresent()) ?
                ResponseEntity.status(HttpStatus.CONFLICT).build() :
                ResponseEntity.created(userService.create(user)).build();
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> logInByEmailPassword(@RequestBody UserDto user)
            throws NoSuchAlgorithmException {
        TokenResponse response = userService.logInByEmailPassword(user);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/auth")
    public ResponseEntity<HttpStatus> authorize(@AccessToken TokenValidationResult result) {
        return ResponseEntity.status(userService.authorize(result)).build();
    }

    @GetMapping("/auth-refresh")
    public ResponseEntity<TokenResponse> updateToken(@RequestHeader("Authorization") String refreshToken)
            throws NoSuchAlgorithmException {
        TokenResponse response = userService.updateToken(refreshToken.split("Bearer ")[1]);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<HttpStatus> logOut(@AccessToken TokenValidationResult result) {
        return (userService.logOut(result)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> delete(@AccessToken TokenValidationResult result) {
        return (userService.delete(result)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}