package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.UserDto;
import com.onehana.server_ilogu.dto.request.BoardCreateRequest;
import com.onehana.server_ilogu.dto.request.BoardModifyRequest;
import com.onehana.server_ilogu.dto.request.CommentRequest;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.BoardResponse;
import com.onehana.server_ilogu.dto.response.CommentResponse;
import com.onehana.server_ilogu.entity.BoardCategory;
import com.onehana.server_ilogu.service.AzureService;
import com.onehana.server_ilogu.service.BoardService;
import com.onehana.server_ilogu.service.ChatGptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final AzureService azureService;
    private final ChatGptService chatGptService;

    @Operation(summary = "피드글 업로드", description = "피드글을 작성한다")
    @PostMapping
    public BaseResponse<Void> createBoard(@RequestBody BoardCreateRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        boardService.createBoard(request.getTitle(), request.getContent(), request.getCategory(), userDto.getEmail());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "피드글 수정", description = "피드글을 수정한다")
    @PutMapping("/{boardId}")
    public BaseResponse<BoardResponse> modifyBoard(@PathVariable Long boardId,
                                              @RequestBody BoardModifyRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        BoardDto boardDto = boardService.modifyBoard(request.getTitle(), request.getContent(), request.getCategory(), userDto.getEmail(), boardId);

        return new BaseResponse<>(BoardResponse.of(boardDto));
    }

    @Operation(summary = "피드글 삭제", description = "피드글을 삭제한다.")
    @DeleteMapping("/{boardId}")
    public BaseResponse<Void> deleteBoard(@PathVariable Long boardId, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        boardService.deleteBoard(userDto.getEmail(), boardId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "피드글 조회", description = "pageable 옵션에 따라 전체 피드글을 조회한다.")
    @GetMapping
    public BaseResponse<Page<BoardResponse>> getBoards(Pageable pageable, Authentication authentication) {
        return new BaseResponse<>(boardService.getBoards(pageable).map(BoardResponse::of));
    }

    @Operation(summary = "카테고리별 피드글 조회", description = "pageable 옵션과 카테고리에 따라 피드글을 조회한다.")
    @GetMapping("/category/{category}")
    public BaseResponse<Page<BoardResponse>> getBoardsByCategory(@PathVariable BoardCategory category, Pageable pageable,
                                                                 Authentication authentication) {
        return new BaseResponse<>(boardService.getBoardsByCategory(category, pageable).map(BoardResponse::of));
    }

    @Operation(summary = "나의 피드글 조회", description = "pageable 옵션에 따라 로그인한 유저의 피드글을 조회한다.")
    @GetMapping("/my")
    public BaseResponse<Page<BoardResponse>> getMyBoards(Pageable pageable, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        return new BaseResponse<>(boardService.getMyBoards(userDto.getEmail(), pageable).map(BoardResponse::of));
    }

    @Operation(summary = "카테고리 별 나의 피드글 조회", description = "pageable 옵션과 카테고리에 따라 로그인한 유저의 피드글을 조회한다.")
    @GetMapping("/my/category/{category}")
    public BaseResponse<Page<BoardResponse>> getMyBoardsByCategory(@PathVariable BoardCategory category,
                                                                   Pageable pageable, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        return new BaseResponse<>(boardService.getMyBoardsByCategory(userDto.getEmail(), category, pageable).map(BoardResponse::of));
    }

    @Operation(summary = "이미지 설명글 생성", description = "이미지를 등록하면 분석해서 관련 글을 작성해준다.")
    @PostMapping("/image/explain")
    public BaseResponse<String> explainImage(@RequestParam List<MultipartFile> file,
                                             @RequestParam String prompt) throws IOException {
        byte[] imageData = file.get(0).getBytes();
        String imageKeyword = azureService.analyzeImage(imageData);

        String res = chatGptService.askQuestionWithPrompt(imageKeyword, prompt);
        return new BaseResponse<>(res);
    }

    @Operation(summary = "댓글 작성", description = "댓글을 작성한다.")
    @PostMapping("/{boardId}/comment")
    public BaseResponse<Void> createComment(@PathVariable Long boardId, @RequestBody CommentRequest request,
                                            Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        boardService.createComment(boardId, request.getParentComment(), request.getComment(), userDto.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.")
    @PutMapping("/{boardId}/comment/{commentId}")
    public BaseResponse<Void> modifyComment(@PathVariable Long boardId, @PathVariable Long commentId,
                                            @RequestBody CommentRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        boardService.modifyComment(commentId, request.getComment(), userDto.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId,
                                            Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        boardService.deleteComment(commentId, userDto.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 조회", description = "해당 게시글의 모든 댓글을 조회한다.")
    @GetMapping("/{boardId}/comments")
    public BaseResponse<Page<CommentResponse>> getComments(@PathVariable Long boardId, Pageable pageable,
                                                           Authentication authentication) {
        return new BaseResponse<>(boardService.getComments(boardId, pageable).map(CommentResponse::fromCommentDto));
    }
}
