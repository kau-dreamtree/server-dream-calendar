package org.standard.dreamcalendar.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.dto.AdminTokenExpirationTestDto;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.util.DtoConverter;
import org.standard.dreamcalendar.util.Encryptor;
import org.standard.dreamcalendar.util.JwtProvider;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final Encryptor encryptor;
    private final JwtProvider tokenProvider;
    private final DtoConverter converter;

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public LogInByEmailPasswordResponse tokenExpirationTest(AdminTokenExpirationTestDto dto)
            throws NoSuchAlgorithmException {

        // Check email address in DB
        User user = userRepository.findById(dto.getId()).orElse(null);

        // Check password in DB
        String givenPassword = encryptor.SHA256(dto.getPassword());

        if (user == null || !givenPassword.equals(user.getPassword())) {
            return null;
        }

        // Save & issue tokens
        String accessToken = tokenProvider.generateForExpirationTest(
                dto.getId(), dto.getTimeUnit(), dto.getAccessExpiration(), TokenType.AccessToken
        );
        String refreshToken = tokenProvider.generateForExpirationTest(
                dto.getId(), dto.getTimeUnit(), dto.getRefreshExpiration(), TokenType.RefreshToken
        );

        user.updateRefreshToken(refreshToken);

        return LogInByEmailPasswordResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
