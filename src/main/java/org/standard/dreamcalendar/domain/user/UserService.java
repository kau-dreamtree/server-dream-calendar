package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.auth.AuthService;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.enums.Role;
import org.standard.dreamcalendar.global.util.DtoConverter;
import org.standard.dreamcalendar.global.util.Encryptor;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final DtoConverter converter;

    @Transactional
    public HttpStatus create(UserDto userDto) throws NoSuchAlgorithmException {
        if (!userRepository.existsByEmail(userDto.getEmail())) {
            userDto.setRole(Role.USER);
            userDto.setPassword(Encryptor.SHA256(userDto.getPassword()));
            User user = userRepository.save(converter.toUserEntity(userDto));
            authService.create(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.CONFLICT;
    }

    public UserDto findById(Long id) {
        return converter.toUserDto(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public HttpStatus update(TokenValidationResult result, UserDto userDto) {
        switch (result.getStatus()) {
            case INVALID:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            default:
                updateUser(userDto);
                return HttpStatus.NO_CONTENT;
        }
    }

    @Transactional
    private void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(IllegalArgumentException::new);
        user.updatePassword(userDto.getPassword());
        user.updateOnSocialLogIn(userDto.getName(), userDto.getPicture());
        userRepository.save(user);
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

}