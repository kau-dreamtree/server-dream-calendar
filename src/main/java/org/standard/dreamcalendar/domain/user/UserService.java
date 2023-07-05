package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.response.UpdateTokenResponse;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.util.DtoConverter;
import org.standard.dreamcalendar.util.Encryptor;
import org.standard.dreamcalendar.util.JwtProvider;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Encryptor encryptor;
    private final JwtProvider tokenProvider;
    private final DtoConverter converter;

    @Transactional
    public Boolean create(UserDto userDto) throws NoSuchAlgorithmException {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return false;
        }

        userDto.setRole(Role.USER);
        userDto.setPassword(encryptor.SHA256(userDto.getPassword()));
        userRepository.save(converter.toUserEntity(userDto));

        return true;

    }

    @Transactional
    public LogInByEmailPasswordResponse logInByEmailPassword(UserDto userDto) throws NoSuchAlgorithmException {

        // Check email address in DB
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);

        // Check password in DB
        String givenPassword = encryptor.SHA256(userDto.getPassword());

        if (user == null || !givenPassword.equals(user.getPassword())) {
            return null;
        }

        // Save & issue tokens
        String accessToken = tokenProvider.generate(user.getId(), TokenType.AccessToken);
        String refreshToken = tokenProvider.generate(user.getId(), TokenType.RefreshToken);

        user.updateRefreshToken(refreshToken);

        return LogInByEmailPasswordResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public HttpStatus logInByAccessToken(TokenValidationResult result) {

        if (result.getStatus() == INVALID) {
            return HttpStatus.BAD_REQUEST;
        }

        if (result.getStatus() == EXPIRED) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.OK;
    }

    @Transactional
    public UpdateTokenResponse updateToken(String refreshToken) {

        User user = userRepository.findByRefreshToken(refreshToken).orElse(null);

        TokenValidationResult result = tokenProvider.validateToken(refreshToken, TokenType.RefreshToken);

        if (user == null || result.getStatus() != VALID) {
            return null;
        }

        String accessToken = tokenProvider.generate(user.getId(), TokenType.AccessToken);
        String newRefreshToken = tokenProvider.generate(user.getId(), TokenType.RefreshToken);

        user.updateRefreshToken(newRefreshToken);

        return UpdateTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();

    }

    @Transactional
    public Boolean logOut(TokenValidationResult result) {

        if (result.getStatus() != VALID) {
            return false;
        }

        User user = userRepository.findById(result.getUserId()).orElse(null);
        user.updateRefreshToken(null);

        return true;
    }

    @Transactional
    public Boolean delete(TokenValidationResult result) {

        if (result.getStatus() != VALID) {
            return false;
        }

        userRepository.deleteById(result.getUserId());

        return true;
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return converter.toUserDto(user);
    }

    public List<UserDto> findUsersByUsername(String username) {
        List<User> userList = userRepository.findByName(username);
        return userList.stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return converter.toUserDto(user);
    }

}