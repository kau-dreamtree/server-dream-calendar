package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.auth.AccessToken;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<HttpStatus> signUp(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.status(userService.create(userDto)).build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> get(@AccessToken TokenValidationResult result) {
        return ResponseEntity.ok(userService.findById(result.getUserId()));
    }

    @PutMapping("/user")
    public ResponseEntity<HttpStatus> update(@AccessToken TokenValidationResult result, @RequestBody UserDto userDto) {
        return ResponseEntity.status(userService.update(result, userDto)).build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> delete(@AccessToken TokenValidationResult result) {
        return ResponseEntity.status(userService.delete(result)).build();
    }

}