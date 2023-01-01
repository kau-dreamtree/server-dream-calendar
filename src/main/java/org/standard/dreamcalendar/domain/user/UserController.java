package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // JWT 발급 코드 추가 예정
        return (userService.create(user)) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto user) throws NoSuchAlgorithmException {
        String jwt = userService.logInByEmailPassword(user);
        log.debug("UserController login()={}", jwt);
        return (jwt == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(jwt);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> findById(@RequestParam("id") Integer userId) {
        UserDto user = userService.findById(userId);
        return (user == null) ? ResponseEntity.notFound().build() : ResponseEntity.status(HttpStatus.OK).body(user);
    }

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