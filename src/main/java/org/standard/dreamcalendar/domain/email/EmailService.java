package org.standard.dreamcalendar.domain.email;

public interface EmailService {
    void sendMail(String subject, String message, String... to);
}
