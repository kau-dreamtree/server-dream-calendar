package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.TokenResponse;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.LoginByAccessTokenResponse;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<HttpStatus> test() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/create")
    public ResponseEntity<LoginByAccessTokenResponse> create(@RequestBody UserDto user) throws NoSuchAlgorithmException {
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).body(new LoginByAccessTokenResponse(HttpStatus.CONFLICT));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> logInByEmailPassword(@RequestBody UserDto user)
            throws NoSuchAlgorithmException {
        TokenResponse tokenResponse = userService.logInByEmailPassword(user);
        return (tokenResponse == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @GetMapping("/auth/login")
    public ResponseEntity<LoginByAccessTokenResponse> loginByAccessToken(@RequestHeader("Authorization") String accessToken) {
        HttpStatus status = userService.logInByAccessToken(accessToken);
        return ResponseEntity.status(status).body(new LoginByAccessTokenResponse(status));
    }

    @GetMapping("/auth/login/update")
    public ResponseEntity<TokenResponse> updateToken(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse tokenResponse = userService.updateToken(refreshToken);
        return (tokenResponse == null) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build() :
                ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return (userDtoList.isEmpty()) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @DeleteMapping("/auth/delete")
    public ResponseEntity<HttpStatus> delete(@RequestHeader("Authorization") String accessToken) {
        return (userService.delete(accessToken)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}