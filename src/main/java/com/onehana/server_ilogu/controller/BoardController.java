package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.response.BaseResponse;
import com.onehana.server_ilogu.dto.response.ChatGptResponse;
import com.onehana.server_ilogu.service.AzureService;
import com.onehana.server_ilogu.service.ChatGptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final AzureService azureService;
    private final ChatGptService chatGptService;

    @Operation(summary = "이미지 설명글 생성", description = "이미지를 등록하면 분석해서 관련 글을 작성해준다.")
    @PostMapping("/image/explain")
    public BaseResponse<String> explainImage(@RequestParam List<MultipartFile> file,
                                     @RequestParam String prompt) throws IOException {
        byte[] imageData = file.get(0).getBytes();
        String imageKeyword = azureService.analyzeImage(imageData);

        String res = chatGptService.askQuestionWithPrompt(imageKeyword, prompt);
        return new BaseResponse<>(res);
    }
}
