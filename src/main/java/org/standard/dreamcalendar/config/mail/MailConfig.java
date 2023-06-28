package org.standard.dreamcalendar.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {

        Properties mailProperties = new Properties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.debug", "true");
        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailProperties.put("mail.smtp.starttls.enable", "true");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("utf-8");

        return mailSender;

    }

}
