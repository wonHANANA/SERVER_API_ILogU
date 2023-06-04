package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    Optional<List<BoardImage>> findByBoardId(Long boardId);
}
