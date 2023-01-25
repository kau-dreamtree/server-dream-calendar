package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.dto.response.CreateResponse;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LoginByAccessTokenResponse;
import org.standard.dreamcalendar.domain.user.dto.response.UpdateTokenResponse;

import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<CreateResponse> create(@RequestBody UserDto user)
            throws NoSuchAlgorithmException {
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).body(new CreateResponse("이미 등록된 이메일입니다."));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<LogInByEmailPasswordResponse> logInByEmailPassword(@RequestBody UserDto user)
            throws NoSuchAlgorithmException {
        LogInByEmailPasswordResponse response = userService.logInByEmailPassword(user);
        return (response == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/auth/login")
    public ResponseEntity<LoginByAccessTokenResponse> loginByAccessToken(
            @RequestHeader("Authorization") String accessToken
    ) {
        HttpStatus status = userService.logInByAccessToken(accessToken);
        return ResponseEntity.status(status).body(new LoginByAccessTokenResponse(status));
    }

    @GetMapping("/auth/login/update")
    public ResponseEntity<UpdateTokenResponse> updateToken(@RequestHeader("Authorization") String refreshToken) {
        UpdateTokenResponse response = userService.updateToken(refreshToken);
        return (response == null) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build() :
                ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<HttpStatus> logOut(@RequestHeader("Authorization") String accessToken) {
        return (userService.logOut(accessToken) == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/auth/delete")
    public ResponseEntity<HttpStatus> delete(@RequestHeader("Authorization") String accessToken) {
        return (userService.delete(accessToken)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}