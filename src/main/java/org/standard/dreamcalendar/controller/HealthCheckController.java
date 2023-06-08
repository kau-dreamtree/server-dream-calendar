package org.standard.dreamcalendar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/test")
    public ResponseEntity<HttpStatus> test() {
        return ResponseEntity.ok().build();
    }

}
