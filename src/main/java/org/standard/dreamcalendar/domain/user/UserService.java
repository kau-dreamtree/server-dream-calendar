package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.domain.user.template.JwtGenerationContext;
import org.standard.dreamcalendar.domain.user.type.Role;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.global.util.DtoConverter;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.JwtProvider;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.standard.dreamcalendar.domain.user.type.TokenValidationStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Encryptor encryptor;
    private final JwtProvider jwtProvider;
    private final DtoConverter converter;
    private final JwtGenerationContext jwtGenerationContext;

    @Transactional
    public URI create(UserDto userDto) throws NoSuchAlgorithmException {
        userDto.setRole(Role.USER);
        userDto.setPassword(encryptor.SHA256(userDto.getPassword()));
        return URI.create("user/" + userRepository.save(converter.toUserEntity(userDto)).getId().toString());
    }

    @Transactional
    public TokenResponse logInByEmailPassword(UserDto userDto) throws NoSuchAlgorithmException {
        return jwtGenerationContext.generateTokenByEmailPassword(
                userDto,
                (user) -> userRepository.existsByEmail(user.getEmail())
                            && encryptor.SHA256(userDto.getPassword()).equals(user.getPassword())
        );
    }

    public HttpStatus authorize(TokenValidationResult result) {

        if (result.getStatus() == INVALID) {
            return HttpStatus.BAD_REQUEST;
        }

        if (result.getStatus() == EXPIRED) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.OK;
    }

    @Transactional
    public TokenResponse updateToken(String refreshToken) throws NoSuchAlgorithmException {
        return jwtGenerationContext.generateTokenByEmailPassword(
                refreshToken,
                (user) -> user != null
                            && jwtProvider.validateToken(refreshToken, TokenType.RefreshToken).getStatus() == VALID
        );
    }

    @Transactional
    public Boolean logOut(TokenValidationResult result) {

        if (result.getStatus() != VALID) {
            return false;
        }
        userRepository.updateRefreshTokenById(null, result.getUserId());
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}