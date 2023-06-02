package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.dto.response.ChatGptResponse;
import com.onehana.server_ilogu.service.AmazonS3Service;
import com.onehana.server_ilogu.service.AzureService;
import com.onehana.server_ilogu.service.ChatGptService;
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

    @PostMapping("/image/explain")
    public ChatGptResponse explainImage(@RequestParam List<MultipartFile> file,
                                        @RequestParam String prompt) throws IOException {
        byte[] imageData = file.get(0).getBytes();
        String imageKeyword = azureService.analyzeImage(imageData);

        return chatGptService.askQuestionWithPrompt(imageKeyword, prompt);
    }
}
