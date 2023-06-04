package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoardAndParentCommentIsNull(Board board, Pageable pageable);
}
