package org.standard.dreamcalendar.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.config.mail.AuthCodeGenerator;
import org.standard.dreamcalendar.util.DtoConverter;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final DtoConverter converter;
    private final JavaMailSender mailSender;

    @Transactional
    public EmailAuth emailAuthProcess(EmailAuthDto emailAuthDto) {

        if (!emailAuthRepository.existsByEmail(emailAuthDto.getEmail())) {
            String code = new AuthCodeGenerator().getAuthCode(6, false);
            emailAuthDto.setCode(code);
            EmailAuth emailAuth = converter.toEmailAuthEntity(emailAuthDto);
            emailAuthRepository.save(emailAuth);
        }

        return emailAuthRepository.findByEmail(emailAuthDto.getEmail()).orElse(null);
    }

    @Transactional
    public Boolean emailAuthCommit(EmailAuthDto emailAuthDto) {

        EmailAuth emailAuth = emailAuthRepository.findByEmail(emailAuthDto.getEmail()).orElse(null);

        if (emailAuth != null && emailAuth.getCode().equals(emailAuthDto.getCode())) {
            emailAuthRepository.delete(emailAuth);
            return true;
        }

        return false;
    }

}
