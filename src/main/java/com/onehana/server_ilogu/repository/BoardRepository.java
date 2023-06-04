package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(BoardCategory category, Pageable pageable);
}
