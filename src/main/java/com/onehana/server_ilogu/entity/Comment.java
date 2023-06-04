package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "post_id_idx", columnList = "post_id")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String comment;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Board board;

    public static Comment of(User user, Board board, String comment, Comment parentComment) {
        Comment commentEntity = new Comment();
        commentEntity.user = user;
        commentEntity.board = board;
        commentEntity.comment = comment;
        commentEntity.parentComment = parentComment;
        return commentEntity;
    }
}
