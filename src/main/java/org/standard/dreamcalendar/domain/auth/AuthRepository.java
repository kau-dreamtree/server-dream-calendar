package org.standard.dreamcalendar.domain.auth;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthRepository extends CrudRepository<AuthInfo, Long> {
    Optional<AuthInfo> findByRefreshToken(String refreshToken);
}
