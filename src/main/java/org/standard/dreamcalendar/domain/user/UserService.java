package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.domain.user.template.TokenGenerationContext;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.global.util.DtoConverter;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.token.AccessTokenProvider;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoConverter converter;
    private final TokenGenerationContext tokenGenerationContext;

    @Transactional
    public URI create(UserDto userDto) throws NoSuchAlgorithmException {
        userDto.setRole(Role.USER);
        userDto.setPassword(Encryptor.SHA256(userDto.getPassword()));
        return URI.create("user/" + userRepository.save(converter.toUserEntity(userDto)).getId().toString());
    }

    @Transactional
    public TokenResponse logInByEmailPassword(UserDto userDto) throws Exception {
        return tokenGenerationContext.generateTokensByEmailPassword(
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

    @Transactional
    public TokenResponse updateToken(String refreshToken) throws Exception {
        return tokenGenerationContext.generateTokensByEmailPassword(refreshToken, Objects::nonNull);
    }

    @Transactional
    public HttpStatus logOut(TokenValidationResult result) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            default:
                userRepository.updateRefreshTokenById(null, result.getUserId());
                return HttpStatus.OK;
        }
    }

    public HttpStatus delete(TokenValidationResult result) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            default:
                userRepository.deleteById(result.getUserId());
                return HttpStatus.NO_CONTENT;
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}