package org.standard.dreamcalendar.domain.email;

import java.util.Arrays;

public class MockEmailService implements EmailService {
    @Override
    public void sendMail(String subject, String message, String... to) {
        System.out.println("이메일 송신\n" +
                "제목: " + subject + "\n" +
                "내용: " + message + "\n" +
                "받는사람: " + Arrays.toString(to) + "\n"
        );
    }
}
