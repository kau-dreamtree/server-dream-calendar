package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.user.model.LogInResponse;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody UserDto user) {
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LogInResponse> logInByEmailPassword(@RequestBody UserDto user) throws NoSuchAlgorithmException {
        LogInResponse logInResponse = userService.logInByEmailPassword(user);
        return (logInResponse == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(logInResponse);
    }

    @GetMapping("/auth/login")
    public ResponseEntity<Object> loginByAccessToken(@RequestHeader String accessToken) {
        HttpStatus status = userService.validateAccessToken(accessToken);
        return ResponseEntity.status(status).build();
    }

//    @GetMapping("/auth/update/access")
//    public ResponseEntity<Object> updateAccessToken(@RequestHeader String refreshToken) {
//
//    }
//
//    @GetMapping("/auth/update/refresh")
//    public ResponseEntity<Object> updateRefreshToken(@RequestHeader String refreshToken) {
//
//    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return (userDtoList.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestParam("id") Integer userId) {
        return (userService.delete(userId)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.notFound().build();
    }

}