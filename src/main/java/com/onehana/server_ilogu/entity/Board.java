package com.onehana.server_ilogu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "user_id"),
        @Index(columnList = "category")
})
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @JsonIgnore
    @JoinTable(
            name = "board_hashtag",
            joinColumns = @JoinColumn(name = "boardId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BoardImage> boardImages = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public static Board toEntity(BoardDto boardDto, User user) {
        Board board = new Board();
        board.title = boardDto.getTitle();
        board.content = boardDto.getContent();
        board.category = boardDto.getCategory();
        board.hashtags = boardDto.getHashtags();
        board.user = user;
        return board;
    }

    public void addHashtag(Hashtag hashtag) {
        if(this.hashtags == null)
            this.hashtags = new LinkedHashSet<>();
        this.getHashtags().add(hashtag);
    }

    public void clearHashtags() {
        this.hashtags.clear();
    }
}
