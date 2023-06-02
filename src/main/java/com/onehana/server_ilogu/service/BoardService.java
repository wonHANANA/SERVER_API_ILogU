package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.BoardRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void create(String title, String body, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        boardRepository.save(Board.of(title, body, user));
    }

    public BoardDto modify(String title, String body, String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        Board post = boardRepository.findById(postId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.POST_NOT_FOUND));

        if (post.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }

        post.setTitle(title);
        post.setContent(body);

        return BoardDto.of(boardRepository.save(post));
    }
}
