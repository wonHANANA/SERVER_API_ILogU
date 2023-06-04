package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardCategory;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.BoardRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void create(String title, String content, BoardCategory category, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        boardRepository.save(Board.of(title, content, category, user));
    }

    public BoardDto modify(String title, String content, BoardCategory category, String email, Long boardId) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        if (board.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }

        board.setTitle(title);
        board.setContent(content);
        board.setCategory(category);

        return BoardDto.of(boardRepository.save(board));
    }

    public void delete(String email, Long boardId) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Board post = boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        if (post.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }
        boardRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<BoardDto> list(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardDto::of);
    }
}
