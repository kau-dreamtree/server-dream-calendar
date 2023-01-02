package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.config.*;
import org.standard.dreamcalendar.domain.user.model.LogInResponse;
import org.standard.dreamcalendar.model.DtoConverter;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private DtoConverter converter;

    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UserDto userDto) {

        try {

            userDto.setPassword(passwordEncoder.sha256(userDto.getPassword()));
            userRepository.save(converter.toUserEntity(userDto));
            return true;

        } catch (NoSuchAlgorithmException e) {

            log.error("UserService create()={}", e);
            return false;

        }

    }

    @Transactional
    public LogInResponse logInByEmailPassword(UserDto userDto) throws NoSuchAlgorithmException {

        // Check email address in DB
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);

        if (user == null)
            return null;

        // Check password in DB
        String givenPassword = passwordEncoder.sha256(userDto.getPassword());
        String expectedPassword = user.getPassword();

        if (!givenPassword.equals(expectedPassword)) {
            log.error("\nGiven: " + givenPassword + "\nExpected: " + expectedPassword);
            return null;
        }

        // Issue JWT
        String accessToken = tokenProvider.generate(String.valueOf(user.getId()), TokenType.AccessToken);
        String refreshToken = tokenProvider.generate(String.valueOf(user.getId()), TokenType.RefreshToken);

        user.saveAccessToken(accessToken);
        user.saveRefreshToken(refreshToken);

        userRepository.save(user);

        return LogInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public HttpStatus logInByAccessToken(String accessToken) {

        HttpStatus status = validateAccessToken(accessToken);


        return status;
    }

    /**
     * 1. 입력된 토큰이 유효하여 ID를 추출할 수 있음 <br>
     * 2. 해당 ID의 사용자가 DB에 존재함 <br>
     * 3. 해당 ID의 accessToken과 입력된 토큰이 일치함 <br>
     * <p> 위 세 조건을 모두 만족하면 return true
     * <p> 1. 토큰이  만료된 경우 401 Unauthorized <br>
     * 2. 토큰이 DB에 없거나 다를 겅우 400 Bad Request
     */
    @Transactional
    public HttpStatus validateAccessToken(String accessToken) {

        TokenValidationResult validation = tokenProvider.validateToken(accessToken, TokenType.AccessToken);

        if (validation.getType() == TokenValidationType.EXPIRED)
            return HttpStatus.UNAUTHORIZED;

        if (validation.getType() == TokenValidationType.INVALID)
            return HttpStatus.BAD_REQUEST;

        User user = userRepository.findById(validation.getUserId()).orElse(null);

        if (user == null)
            return HttpStatus.BAD_REQUEST;

        if (!user.getAccessToken().equals(accessToken))
            return HttpStatus.BAD_REQUEST;

        return HttpStatus.ACCEPTED;
    }

//    @Transactional
//    public Boolean validateRefreshToken(String refreshToken) {
//
//        TokenValidationResult validation = tokenProvider.validateToken(refreshToken, TokenType.RefreshToken);
//
//        User user = userRepository.findById(userId).orElse(null);
//
//        return user != null && user.getAccessToken().equals(refreshToken);
//    }

//    @Transactional
//    public String updateAccessToken(String refreshToken) {
//
//
//    }
//
//    @Transactional
//    public String updateRefreshToken(String refreshToken) {
//
//    }



    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        return converter.toUserDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findUsersByUsername(String username) {
        List<User> userList = userRepository.findByUsername(username);
        return userList.stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return converter.toUserDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            return false;

        userRepository.deleteById(id);
        return true;

    }

}