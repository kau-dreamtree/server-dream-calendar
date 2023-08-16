package org.standard.dreamcalendar.domain.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmailTest {

    EmailService emailService = new MockEmailService();

    @Test
    void send() {
        String subject = "테스트 제목";
        String message = "테스트 내용";
        String recipient = "sjw_3@naver.com";
        emailService.sendMail(subject, message, recipient);
    }
}
