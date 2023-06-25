package com.onehana.server_ilogu.dto.response;

import com.onehana.server_ilogu.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String comment;
    private String nickname;
    private Long boardId;
    private Long parentComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> childComments;

    public static CommentResponse fromCommentDto(CommentDto commentDto) {
        List<CommentResponse> childCommentsDto = commentDto.getChildComments().stream()
                .map(CommentResponse::fromCommentDto)
                .collect(Collectors.toList());

        return new CommentResponse(
                commentDto.getId(),
                commentDto.getComment(),
                commentDto.getNickname(),
                commentDto.getBoardId(),
                commentDto.getParentCommentId(),
                commentDto.getCreatedAt(),
                commentDto.getUpdatedAt(),
                childCommentsDto
        );
    }
}