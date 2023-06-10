package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String token);

    Optional<User> findByNickname(String nickName);
}
