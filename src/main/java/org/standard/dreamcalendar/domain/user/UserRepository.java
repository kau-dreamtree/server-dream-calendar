package org.standard.dreamcalendar.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.standard.dreamcalendar.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.accessToken = ?2 where u.id = ?1")
    int updateAccessToken(@NonNull Integer id, @NonNull String accessToken);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.refreshToken = ?2 where u.id = ?1")
    int updateRefreshToken(@NonNull Integer id, @NonNull String refreshToken);

    List<User> findByName(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByAccessToken(String accessToken);

    Optional<User> findByRefreshToken(String refreshToken);

}
