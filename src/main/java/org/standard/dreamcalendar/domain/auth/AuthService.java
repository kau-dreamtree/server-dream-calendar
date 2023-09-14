package org.standard.dreamcalendar.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.standard.dreamcalendar.domain.auth.template.TokenGenerationContext;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.global.util.Encryptor;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final TokenGenerationContext tokenGenerationContext;

    public void create(User user) {
        authRepository.save(AuthInfo.builder().userId(user.getId()).build());
    }

    public TokenResponse logInByEmailPassword(UserDto userDto) throws Exception {
        return tokenGenerationContext.authorizeAndGenerateTokens(
                userDto,
                (user) -> userRepository.existsByEmail(user.getEmail())
                        && Encryptor.SHA256(userDto.getPassword()).equals(user.getPassword())
        );
    }

    public HttpStatus authorize(TokenValidationResult result) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            default:
                return HttpStatus.OK;
        }
    }

    public TokenResponse updateToken(String refreshToken) throws Exception {
        return tokenGenerationContext.authorizeAndGenerateTokens(refreshToken, Objects::nonNull);
    }

    public HttpStatus logOut(TokenValidationResult result) throws EntityNotFoundException {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            default:
                AuthInfo authInfo = authRepository.findById(result.getUserId()).orElseThrow(EntityNotFoundException::new);
                authInfo.updateRefreshToken(null);
                authRepository.save(authInfo);
                return HttpStatus.OK;
        }
    }
}
