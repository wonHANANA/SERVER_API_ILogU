package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
