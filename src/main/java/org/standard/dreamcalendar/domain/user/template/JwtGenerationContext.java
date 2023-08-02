package org.standard.dreamcalendar.domain.user.template;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.User;
import org.standard.dreamcalendar.domain.user.UserRepository;
import org.standard.dreamcalendar.domain.user.dto.UserDto;
import org.standard.dreamcalendar.domain.user.dto.response.TokenResponse;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.util.JwtProvider;

import javax.persistence.EntityNotFoundException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Component
public class JwtGenerationContext {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public <T> TokenResponse generateTokenByEmailPassword(T userInfo, JwtGenerationCallback callback) throws NoSuchAlgorithmException {

        User user = findUser(userInfo);
        boolean validation = callback.validateUser(user);

        if (validation) {

            String accessToken = jwtProvider.generate(user.getId(), TokenType.AccessToken);
            String refreshToken = jwtProvider.generate(user.getId(), TokenType.RefreshToken);

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
