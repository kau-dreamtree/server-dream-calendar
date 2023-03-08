package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.response.UpdateTokenResponse;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<HttpStatus> create(@RequestBody UserDto user) throws NoSuchAlgorithmException {
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/auth")
    public ResponseEntity<LogInByEmailPasswordResponse> logInByEmailPassword(@RequestBody UserDto user)
            throws NoSuchAlgorithmException {
        LogInByEmailPasswordResponse response = userService.logInByEmailPassword(user);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/auth")
    public ResponseEntity<HttpStatus> authorize(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.status(userService.logInByAccessToken(accessToken)).build();
    }

    @GetMapping("/auth-refresh")
    public ResponseEntity<UpdateTokenResponse> updateToken(@RequestHeader("Authorization") String refreshToken) {
        UpdateTokenResponse response = userService.updateToken(refreshToken);
        return (response != null) ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<HttpStatus> logOut(@RequestHeader("Authorization") String accessToken) {
        return (userService.logOut(accessToken)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> delete(@RequestHeader("Authorization") String accessToken) {
        return (userService.delete(accessToken)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}