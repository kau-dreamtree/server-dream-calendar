package org.standard.dreamcalendar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public class TestController {

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/test")
    public ResponseEntity<HttpStatus> test(@RequestHeader("Authorization") String authorization) {

        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
