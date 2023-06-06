package org.standard.dreamcalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestProfileController {

    private final Environment environment;

    @GetMapping("/test-profile")
    public String profile() {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        List<String> testProfiles = Arrays.asList("test-1", "test-2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return profiles.stream()
                .filter(testProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
