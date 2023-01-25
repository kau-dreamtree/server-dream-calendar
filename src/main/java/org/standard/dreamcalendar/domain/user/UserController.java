package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.standard.dreamcalendar.domain.schedule.ScheduleService;
import org.standard.dreamcalendar.domain.user.dto.TokenResponse;
import org.standard.dreamcalendar.domain.user.dto.UserDto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/*
* Schedule 관리를 위한 import 구문 -> 추후 제거
 */
import org.standard.dreamcalendar.domain.schedule.model.ScheduleDto;

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

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody UserDto user) {
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> logInByEmailPassword(@RequestBody UserDto user)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        TokenResponse tokenResponse = userService.logInByEmailPassword(user);
        return (tokenResponse == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/auth/login")
    public ResponseEntity<HttpStatus> loginByAccessToken(@RequestHeader String accessToken)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        HttpStatus status = userService.logInByAccessToken(accessToken);
        return ResponseEntity.status(status).build();
    }

    @GetMapping("/auth/update/access")
    public ResponseEntity<TokenResponse> updateAccessToken(@RequestHeader String refreshToken)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        TokenResponse tokenResponse = userService.updateAccessToken(refreshToken);
        return (tokenResponse == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return (userDtoList.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Integer userId) {
        return (userService.delete(userId)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.notFound().build();
    }

}