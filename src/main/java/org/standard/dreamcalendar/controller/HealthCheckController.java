package org.standard.dreamcalendar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${spring.security.user.password}")
    protected String adminAuth;

    @GetMapping("/test")
    ResponseEntity test(@RequestHeader("Authorization") String authorization) {
        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
