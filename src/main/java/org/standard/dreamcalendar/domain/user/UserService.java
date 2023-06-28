package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.util.Encryptor;
import org.standard.dreamcalendar.util.JwtProvider;
import org.standard.dreamcalendar.domain.user.dto.response.UpdateTokenResponse;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;
import org.standard.dreamcalendar.util.DtoConverter;

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

    /**
     * 예외 <br>
     * 1. 클라이언트가 입력한 이메일이 DB에 없는 경우 <br>
     * 2. 클라이언트가 입력한 비밀번호가 DB의 비밀번호와 일치하지 않는 경우 <br>
     * @param userDto
     * @return UpdateTokenResponse
     * @throws NoSuchAlgorithmException
     */
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
        String accessToken = tokenProvider.generate(user.getEmail(), TokenType.AccessToken);
        String refreshToken = tokenProvider.generate(user.getEmail(), TokenType.RefreshToken);

        user.updateAccessToken(accessToken);
        user.updateRefreshToken(refreshToken);

        return LogInByEmailPasswordResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 아래 세 조건을 모두 만족하면 ACCEPTED <br>
     * <p>1. 입력된 토큰이 유효하여 ID를 추출할 수 있음 <br>
     * 2. 해당 ID의 사용자가 DB에 존재함 <br>
     * 3. 해당 ID의 accessToken과 입력된 토큰이 일치함 <br>
     * <p> 예외 <br>
     * <p>1. 토큰이 DB에 없거나 다를 겅우 400 Bad Request <br>
     * 2. 토큰이  만료된 경우 401 Unauthorized
     * @param accessToken
     * @return HttpStatus
     */
    public HttpStatus logInByAccessToken(String accessToken) {

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        TokenValidationStatus validation = tokenProvider.validateToken(accessToken, TokenType.AccessToken);

        if (user == null || validation == INVALID) {
            return HttpStatus.BAD_REQUEST;
        }

        if (validation == EXPIRED) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.OK;
    }

    /**
     * 1. Refresh token이 서버와 일치하는지 확인 <br>
     * 2. 정상이고 만료되지 않은 경우 access token만 갱신하여 return <br>
     * 3. 정상이고 곧 만료되는 경우 두 토큰 모두 갱신하여 return <br>
     * 4. DB에 없거나 만료된 경우 return null
     *
     *
     * @param refreshToken
     * @return UpdateTokenResponse
     */
    @Transactional
    public UpdateTokenResponse updateToken(String refreshToken) {

        User user = userRepository.findByRefreshToken(refreshToken).orElse(null);

        TokenValidationStatus validation = tokenProvider.validateToken(refreshToken, TokenType.RefreshToken);

        if (user == null || validation == EXPIRED || validation == INVALID) {
            return null;
        }

        String message = "Access Token Updated";

        if (validation == UPDATE) {
            String newRefreshToken = tokenProvider.generate(user.getEmail(), TokenType.RefreshToken);
            user.updateRefreshToken(newRefreshToken);
            message = "Access & Refresh Token Updated";
        }

        String accessToken = tokenProvider.generate(user.getEmail(), TokenType.AccessToken);

        user.updateAccessToken(accessToken);

        return UpdateTokenResponse.builder()
                .message(message)
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .build();

    }

    @Transactional
    public Boolean logOut(String accessToken) {

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        if (user == null) {
            return false;
        }

        userRepository.updateAccessTokenAndRefreshToken(user.getId(), null, null);

        return true;
    }

    @Transactional
    public Boolean delete(String accessToken) {

        User user = userRepository.findByAccessToken(accessToken).orElse(null);
        if (user == null) {
            return false;
        }

        userRepository.deleteById(user.getId());

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