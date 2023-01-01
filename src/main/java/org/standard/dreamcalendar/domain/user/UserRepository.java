package org.standard.dreamcalendar.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
