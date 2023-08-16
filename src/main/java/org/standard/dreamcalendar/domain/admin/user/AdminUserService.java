package org.standard.dreamcalendar.domain.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.global.util.DtoConverter;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.token.TokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final DtoConverter converter;

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public LogInByEmailPasswordResponse tokenExpirationTest(AdminTokenExpirationTestDto dto) throws Exception {

        // Check email address in DB
        User user = userRepository.findById(dto.getId()).orElse(null);

        // Check password in DB
        String givenPassword = Encryptor.SHA256(dto.getPassword());

        if (user == null || !givenPassword.equals(user.getPassword())) {
            return null;
        }

        // Save & issue tokens
        String accessToken = accessTokenProvider.generate(dto.getId(), dto.getTimeUnit(), dto.getAccessExpiration());
        String refreshToken = refreshTokenProvider.generate(dto.getId());

        user.updateRefreshToken(refreshToken);

        return new LogInByEmailPasswordResponse(accessToken, refreshToken);
    }

}
