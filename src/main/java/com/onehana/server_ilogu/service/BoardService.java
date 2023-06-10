package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.BoardListDto;
import com.onehana.server_ilogu.dto.CommentDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.*;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.BoardLikeRepository;
import com.onehana.server_ilogu.repository.BoardRepository;
import com.onehana.server_ilogu.repository.CommentRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final UserService userService;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Service amazonS3Service;
    private final HashTagService hashTagService;

    public BoardDto createBoard(BoardDto boardDto, String email, List<MultipartFile> files) {
        User user = userService.getUserOrException(email);
        Board board = boardRepository.save(Board.toEntity(boardDto, user));

        hashTagService.createTagList(board);

        if (files != null)
            amazonS3Service.uploadBoardImages(files, BoardDto.of(board));

        return BoardDto.of(board);
    }

    public BoardDto modifyBoard(String title, String content, BoardCategory category, String email, Long boardId) {
        User user = userService.getUserOrException(email);
        Board board = getBoardOrException(boardId);

        if (board.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }

        board.setTitle(title);
        board.setContent(content);
        board.setCategory(category);
        board.clearHashtags();
        hashTagService.createTagList(board);

        return BoardDto.of(boardRepository.save(board));
    }

    public void deleteBoard(String email, Long boardId) {
        User user = userService.getUserOrException(email);
        Board board = getBoardOrException(boardId);

        if (board.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardListDto> getBoards(Pageable pageable, String email) {
        User user = userService.getUserOrException(email);

        return boardRepository.findAll(pageable)
                .map(board -> {
                    int likesCount = countLike(board.getId());
                    int commentsCount = countComments(board.getId());
                    boolean isLiked = isLiked(board.getId(), user.getId());
                    return BoardListDto.of(board, likesCount, commentsCount, isLiked);
                });
    }

    @Transactional(readOnly = true)
    public Page<BoardListDto> getBoardsByCategory(BoardCategory category, Pageable pageable, String email) {
        User user = userService.getUserOrException(email);

        return boardRepository.findByCategory(category, pageable)
                .map(board -> {
                    int likesCount = countLike(board.getId());
                    int commentsCount = countComments(board.getId());
                    boolean isLiked = isLiked(board.getId(), user.getId());
                    return BoardListDto.of(board, likesCount, commentsCount, isLiked);
                });
    }

    @Transactional(readOnly = true)
    public Page<BoardListDto> getMyBoards(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        return boardRepository.findAllByUser(user, pageable)
                .map(board -> {
                    int likesCount = countLike(board.getId());
                    int commentsCount = countComments(board.getId());
                    boolean isLiked = isLiked(board.getId(), user.getId());
                    return BoardListDto.of(board, likesCount, commentsCount, isLiked);
                });
    }

    @Transactional(readOnly = true)
    public Page<BoardListDto> getMyBoardsByCategory(String email, BoardCategory category, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        return boardRepository.findByUserAndCategory(user, category, pageable)
                .map(board -> {
                    int likesCount = countLike(board.getId());
                    int commentsCount = countComments(board.getId());
                    boolean isLiked = isLiked(board.getId(), user.getId());
                    return BoardListDto.of(board, likesCount, commentsCount, isLiked);
                });
    }

    public void createComment(Long boardId, Long parentCommentId, String comment, String email) {
        User user = userService.getUserOrException(email);
        Board board = getBoardOrException(boardId);

        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                    new BaseException(BaseResponseStatus.COMMENT_NOT_FOUND));
        }
        commentRepository.save(Comment.of(user, board, comment, parentComment));
    }

    public void modifyComment(Long commentId, String newComment, String email) {
        User user = userService.getUserOrException(email);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.COMMENT_NOT_FOUND));

        if (comment.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }
        comment.setComment(newComment);
    }

    public void deleteComment(Long commentId, String email) {
        User user = userService.getUserOrException(email);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.COMMENT_NOT_FOUND));

        if (comment.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> getComments(Long boardId, Pageable pageable) {
        Board board = getBoardOrException(boardId);

        return commentRepository.findAllByBoardAndParentCommentIsNull(board, pageable)
                .map(CommentDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public int countComments(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        return commentRepository.countByBoard(board);
    }

    public int like(Long boardId, String email) {
        User user = userService.getUserOrException(email);
        Board board = getBoardOrException(boardId);

        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board);

        if (boardLike == null) {
            boardLikeRepository.save(BoardLike.of(user, board));
        } else {
            boardLikeRepository.delete(boardLike);
        }
        return countLike(boardId);
    }

    public int countLike(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        return boardLikeRepository.countByBoard(board);
    }

    public boolean isLiked(Long boardId, Long userId) {
        return boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    @Transactional(readOnly = true)
    private Board getBoardOrException(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
    }
}
