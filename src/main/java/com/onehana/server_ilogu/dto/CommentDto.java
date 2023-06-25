package com.onehana.server_ilogu.dto;

import com.onehana.server_ilogu.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String comment;
    private String nickname;
    private Long boardId;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> childComments;

    public static CommentDto fromEntity(Comment comment) {
        List<CommentDto> childCommentsDto = new ArrayList<>();
        if (comment.getChildComments() != null) {
            childCommentsDto = comment.getChildComments().stream()
                    .map(CommentDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getNickname(),
                comment.getBoard().getId(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                childCommentsDto
        );
    }
}
