package org.standard.dreamcalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment environment;

    @Value("${spring.security.user.password}")
    private String adminAuth;

    @GetMapping("/profile")
    public ResponseEntity<String> profile(@RequestHeader("Authorization") String authorization) {
        if (!authorization.equals(adminAuth)) {
            return ResponseEntity.badRequest().build();
        }
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real-1", "real-2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return ResponseEntity.status(HttpStatus.OK).body(profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile));
    }
}