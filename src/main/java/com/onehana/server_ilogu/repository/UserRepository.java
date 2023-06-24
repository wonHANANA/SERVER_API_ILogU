package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByRefreshToken(String token);

    @Query("SELECT u FROM User u WHERE u.family.id = " +
            "(SELECT f.id FROM Family f JOIN User u ON u.family.id = f.id WHERE u.email = :email) " +
            "ORDER BY u.depositAccount.sendToChild DESC")
    List<User> findFamilyMembersOrderBySendToChildDesc(@Param("email") String email);

    List<User> findByFamily(Family family);

    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);
}
