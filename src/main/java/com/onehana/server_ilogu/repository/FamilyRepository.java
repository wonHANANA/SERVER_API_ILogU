package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    Optional<Family> findByInviteCode(String inviteCode);

    Optional<Family> findByFamilyName(String familyName);
}
