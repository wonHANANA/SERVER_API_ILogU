package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardLike;
import com.onehana.server_ilogu.entity.Family;
import com.onehana.server_ilogu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    BoardLike findByUserAndBoard(User user, Board board);

    @Query(value = "select count(*) from BoardLike like where like.board =:board")
    Integer countByBoard(@Param("board") Board board);

    int countByUserAndBoard_User_Family(User user, Family family);

    Boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
