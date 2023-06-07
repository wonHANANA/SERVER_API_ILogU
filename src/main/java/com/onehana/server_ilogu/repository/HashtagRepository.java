package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByHashtagName(String hashtagName);
}
