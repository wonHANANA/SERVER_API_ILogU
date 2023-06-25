package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.BoardDetailDto;
import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.request.BoardCreateRequest;
import com.onehana.server_ilogu.dto.request.BoardModifyRequest;
import com.onehana.server_ilogu.dto.request.CommentRequest;
import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.BoardListResponse;
import com.onehana.server_ilogu.dto.response.CommentResponse;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.service.AmazonS3Service;
import com.onehana.server_ilogu.service.AzureService;
import com.onehana.server_ilogu.service.BoardService;
import com.onehana.server_ilogu.service.ChatGptService;
import com.onehana.server_ilogu.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final AmazonS3Service amazonS3Service;
    private final AzureService azureService;
    private final ChatGptService chatGptService;

    @Operation(summary = "피드글 업로드", description = "피드글을 작성한다", tags = "피드")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<Void> createBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestPart BoardCreateRequest request,
                                          @RequestPart(required = false) List<MultipartFile> files) {
        boardService.createBoard(BoardDto.of(request), userDetails.getEmail(), files);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "피드글 수정", description = "피드글을 수정한다", tags = "피드")
    @PutMapping("/{boardId}")
    public BaseResponse<Void> modifyBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long boardId,
                                          @RequestBody BoardModifyRequest request) {
        boardService.modifyBoard(request.getTitle(), request.getContent(),
                request.getCategory(), userDetails.getEmail(), boardId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "피드글 삭제", description = "피드글을 삭제한다.", tags = "피드")
    @DeleteMapping("/{boardId}")
    public BaseResponse<Void> deleteBoard(@PathVariable Long boardId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        amazonS3Service.deleteAllBoardImages(userDetails.getEmail(), boardId);
        boardService.deleteBoard(userDetails.getEmail(), boardId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "피드글 단건 조회", description = "피드글 상세 정보를 댓글과 함께 조회한다.", tags = "피드조회")
    @GetMapping("/{boardId}")
    public BaseResponse<BoardDetailDto> getBoardWithComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable Long boardId, Pageable pageable) {
        BoardDetailDto boardDetail = boardService.getBoardWithComments(boardId, userDetails.getEmail(), pageable);
        return new BaseResponse<>(boardDetail);
    }

    @Operation(summary = "피드글 리스트 조회", description = "pageable 옵션에 따라 전체 피드글을 조회한다.", tags = "피드조회")
    @GetMapping
    public BaseResponse<Page<BoardListResponse>> getBoards(Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(boardService.getBoards(pageable, userDetails.getEmail()).map(BoardListResponse::of));
    }

    @Operation(summary = "카테고리별 피드글 조회", description = "pageable 옵션과 카테고리에 따라 피드글을 조회한다.", tags = "피드조회")
    @GetMapping("/category/{category}")
    public BaseResponse<Page<BoardListResponse>> getBoardsByCategory(@PathVariable BoardCategory category, Pageable pageable,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(boardService.getBoardsByCategory(category, pageable, userDetails.getEmail()).map(BoardListResponse::of));
    }

    @Operation(summary = "나의 피드글 조회", description = "pageable 옵션에 따라 로그인한 유저의 피드글을 조회한다.", tags = "피드조회")
    @GetMapping("/my")
    public BaseResponse<Page<BoardListResponse>> getMyBoards(Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return new BaseResponse<>(boardService.getMyBoards(userDetails.getEmail(), pageable).map(BoardListResponse::of));
    }

    @Operation(summary = "카테고리 별 나의 피드글 조회", description = "pageable 옵션과 카테고리에 따라 로그인한 유저의 피드글을 조회한다.", tags = "피드조회")
    @GetMapping("/my/category/{category}")
    public BaseResponse<Page<BoardListResponse>> getMyBoardsByCategory(@PathVariable BoardCategory category, Pageable pageable,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        return new BaseResponse<>(boardService.getMyBoardsByCategory(userDetails.getEmail(), category, pageable).map(BoardListResponse::of));
    }

    @Operation(summary = "이미지 설명글 생성", description = "이미지를 등록하면 분석해서 관련 글을 작성해준다.", tags = "피드")
    @PostMapping("/image/explain")
    public BaseResponse<String> explainImage(@RequestParam List<MultipartFile> file,
                                             @RequestParam String prompt) throws IOException {
        byte[] imageData = file.get(0).getBytes();
        String imageKeyword = azureService.analyzeImage(imageData);

        String res = chatGptService.askQuestionWithPrompt(imageKeyword, prompt);
        return new BaseResponse<>(res);
    }

    @Operation(summary = "좋아요", description = "이미 좋아요 한 게시글은 좋아요가 삭제된다.", tags = "피드")
    @PutMapping("/like/{boardId}")
    public BaseResponse<Integer> like(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return new BaseResponse<>(boardService.like(boardId, userDetails.getEmail()));
    }

    @Operation(summary = "댓글 작성", description = "댓글을 작성한다.", tags = "댓글")
    @PostMapping("/{boardId}/comment")
    public BaseResponse<Void> createComment(@PathVariable Long boardId, @RequestBody CommentRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boardService.createComment(boardId, request.getParentComment(), request.getComment(), userDetails.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.", tags = "댓글")
    @PutMapping("/{boardId}/comment/{commentId}")
    public BaseResponse<Void> modifyComment(@PathVariable Long boardId, @PathVariable Long commentId,
                                            @RequestBody CommentRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        boardService.modifyComment(commentId, request.getComment(), userDetails.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.", tags = "댓글")
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boardService.deleteComment(commentId, userDetails.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @Operation(summary = "댓글 조회", description = "해당 게시글의 모든 댓글을 조회한다.", tags = "댓글")
    @GetMapping("/{boardId}/comments")
    public BaseResponse<Page<CommentResponse>> getComments(@PathVariable Long boardId, Pageable pageable) {
        return new BaseResponse<>(boardService.getComments(boardId, pageable).map(CommentResponse::fromCommentDto));
    }
}
