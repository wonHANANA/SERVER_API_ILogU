package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Board of(String title, String content, BoardCategory category, User user) {
        Board board = new Board();
        board.title = title;
        board.content = content;
        board.category = category;
        board.user = user;
        return board;
    }
}
