package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByRefreshToken(String token);

    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);
}
