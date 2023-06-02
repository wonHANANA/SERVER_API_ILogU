package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private User user;

    public static BoardDto of(Board post) {
        return new BoardDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser()
        );
    }
}
