package org.standard.dreamcalendar.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    DtoConverter converter;

    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UserDto userDto) {
        try {
            userDto.setPassword(PasswordEncoder.sha256(userDto.getPassword()));
            userRepository.save(converter.toUserEntity(userDto));
            return true;
        } catch (NoSuchAlgorithmException e) {
            log.error("UserService create()={}", e);
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map(converter::toUserDto).collect(Collectors.toList());
        return userDtoList;
    }

    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).get();
        UserDto userDto = converter.toUserDto(user);
        return userDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findUsersByUsername(String username) {
        List<User> userList = userRepository.findByUsername(username);
        List<UserDto> userDtoList = userList.stream().map(converter::toUserDto).collect(Collectors.toList());
        return userDtoList;
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        UserDto userDto = converter.toUserDto(user);
        return userDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

}