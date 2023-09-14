package org.standard.dreamcalendar.domain.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.auth.AuthInfo;
import org.standard.dreamcalendar.domain.auth.AuthRepository;
import org.standard.dreamcalendar.domain.auth.template.TokenGenerationContext;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.LogInByEmailPasswordResponse;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.global.util.DtoConverter;
import org.standard.dreamcalendar.global.util.Encryptor;
import org.standard.dreamcalendar.global.util.token.TokenProvider;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminAuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final DtoConverter converter;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(converter::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public TokenResponse tokenExpirationTest(AdminTokenExpirationTestDto dto) throws Exception {

        // Check email address in DB
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(EntityNotFoundException::new);

        // Check password in DB
        if (!Encryptor.SHA256(dto.getPassword()).equals(user.getPassword())) {
            return null;
        }

        // Issue tokens
        String accessToken = accessTokenProvider.generate(dto.getId(), dto.getTimeUnit(), dto.getAccessExpiration());
        String refreshToken = refreshTokenProvider.generate(dto.getId());

        // Save refreshToken
        AuthInfo authInfo = authRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new);
        authInfo.updateRefreshToken(refreshToken);
        authRepository.save(authInfo);

        return new LogInByEmailPasswordResponse(accessToken, refreshToken);
    }

}
