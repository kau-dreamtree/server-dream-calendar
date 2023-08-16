package org.standard.dreamcalendar.domain.user.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.global.util.token.TokenProvider;

@RequiredArgsConstructor
@Component
public class TokenGenerationContext {

    private final UserRepository userRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;

    @Transactional
    public <T> TokenResponse generateTokensByEmailPassword(T userInfo, TokenValidationCallback callback) throws Exception {

        User user = findUser(userInfo);
        boolean validation = callback.validate(user);

        if (validation) {

            String accessToken = accessTokenProvider.generate(user.getId());
            String refreshToken = refreshTokenProvider.generate(user.getId());

            user.updateRefreshToken(refreshToken);
            userRepository.save(user);

            return new TokenResponse(accessToken, refreshToken);
        }

        return null;
    }

    private <T> User findUser(T userInfo) {
        if (userInfo instanceof UserDto) {
            return userRepository.findByEmail(((UserDto) userInfo).getEmail()).orElse(null);
        }
        if (userInfo instanceof String) {
            return userRepository.findByRefreshToken((String) userInfo).orElse(null);
        }
        return null;
    }
}
