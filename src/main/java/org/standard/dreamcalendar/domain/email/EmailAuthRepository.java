package org.standard.dreamcalendar.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    @Query("update EmailAuth e set e.code = ?2 where e.email = ?1")
    void updateCode(String email, String code);

    Optional<EmailAuth> findByEmail(String email);

    boolean existsByEmail(String email);
}
