package org.standard.dreamcalendar.domain.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Id;

@Getter
@RedisHash(value = "refresh_token")
@NoArgsConstructor
public class AuthInfo {

    @Id
    private Long id;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long ttl;

    @Builder
    public AuthInfo(@NonNull Long userId, @Nullable String refreshToken, @NonNull Long expireMilli) {
        this.id = userId;
        this.refreshToken = refreshToken;
        this.ttl = expireMilli;
    }

    public void updateRefreshToken(@Nullable String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
