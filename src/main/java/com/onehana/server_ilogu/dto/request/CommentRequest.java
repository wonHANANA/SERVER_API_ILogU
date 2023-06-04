package com.onehana.server_ilogu.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long parentComment;
    private String comment;
}
