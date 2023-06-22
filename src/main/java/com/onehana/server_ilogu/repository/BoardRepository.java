package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUser(User user, Pageable pageable);
    Page<Board> findByCategory(BoardCategory category, Pageable pageable);

    Page<Board> findByUserAndCategory(User user, BoardCategory category, Pageable pageable);

    Page<Board> findAllByUser_Family_Id(Long familyId, Pageable pageable);
}
