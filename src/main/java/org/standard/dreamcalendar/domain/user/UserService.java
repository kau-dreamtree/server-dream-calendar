package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.config.JwtTokenProvider;
import org.standard.dreamcalendar.config.PasswordEncoder;
import org.standard.dreamcalendar.models.DtoConverter;

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
    public String logInByEmailPassword(UserDto userDto) throws NoSuchAlgorithmException {
            // 이메일 있는지 확인
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);

        if (user == null)
            return null;

        // 비밀번호 맞는지 확인
        String givenPassword = passwordEncoder.sha256(userDto.getPassword());
        String expectedPassword = user.getPassword();

        if (!givenPassword.equals(expectedPassword)) {
            log.error("\nGiven: " + givenPassword + "\nExpected: " + expectedPassword);
            return null;
        }

        // JWT 발급
        return tokenProvider.generate(user.getId().toString());
    }

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