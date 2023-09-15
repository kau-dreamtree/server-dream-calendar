package org.standard.dreamcalendar.domain.auth.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.auth.AuthInfo;
import org.standard.dreamcalendar.domain.auth.AuthRepository;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.global.util.token.TokenProvider;

@RequiredArgsConstructor
@Component
public class TokenGenerationContext {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;

    @Transactional
    public <T> TokenResponse authorizeAndGenerateTokens(T userInfo, TokenValidationCallback callback) throws Exception {

        User user = findUser(userInfo);
        boolean authorized = callback.validate(user);

        if (authorized) {

            String accessToken = accessTokenProvider.generate(user.getId());
            String refreshToken = refreshTokenProvider.generate(user.getId());

            AuthInfo authInfo = authRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
            authInfo.updateRefreshToken(refreshToken);
            authRepository.save(authInfo);

            return new TokenResponse(accessToken, refreshToken);
        }

        return null;
    }

    private <T> User findUser(T userInfo) {
        if (userInfo instanceof UserDto) {
            return userRepository.findByEmail(((UserDto) userInfo).getEmail()).orElse(null);
        }
        if (userInfo instanceof String) {
            AuthInfo authInfo = authRepository.findByRefreshToken((String) userInfo).orElseThrow(IllegalArgumentException::new);
            return userRepository.findById(authInfo.getId()).orElse(null);
        }
        return null;
    }
}
