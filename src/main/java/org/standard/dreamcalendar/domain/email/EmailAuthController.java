package org.standard.dreamcalendar.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/auth/email")
    public ResponseEntity<EmailAuth> emailAuthProcess(@RequestBody EmailAuthDto emailAuthDto) {
        EmailAuth emailAuth = emailAuthService.emailAuthProcess(emailAuthDto);
        return ResponseEntity.status(HttpStatus.OK).body(emailAuth);
    }

    @DeleteMapping("/auth/email")
    public ResponseEntity<HttpStatus> emailAuthCommit(@RequestBody EmailAuthDto emailAuthDto) {
        return (emailAuthService.emailAuthCommit(emailAuthDto))
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
