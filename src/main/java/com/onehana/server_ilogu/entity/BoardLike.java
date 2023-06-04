package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board ;

    public static BoardLike of(User user, Board board) {
        BoardLike boardLike = new BoardLike();
        boardLike.user = user;
        boardLike.board = board;
        return boardLike;
    }
}
