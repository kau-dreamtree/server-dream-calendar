package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody UserDto user) {

        Boolean isSuccess = userService.create(user);

        if (isSuccess)
            return ResponseEntity.status(HttpStatus.CREATED).build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> findById(@RequestParam("id") Integer userId) {
        UserDto user = userService.findById(userId);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestParam("id") Integer userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}