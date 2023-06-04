package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.config.ChatGptConfig;
import com.onehana.server_ilogu.dto.MessageDto;
import com.onehana.server_ilogu.dto.request.ChatGptRequest;
import com.onehana.server_ilogu.dto.response.ChatGptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chat-gpt.api-key}")
    private String API_KEY;

    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponse.class);

        return responseEntity.getBody();
    }

    public String askQuestionWithPrompt(String message, String prompt) {
        String newMsg = message + prompt;

        MessageDto messageDto = MessageDto.builder()
                .role("user")
                .content(newMsg)
                .build();

        System.out.println("gpt한테 한 질문 : " + newMsg);

        ChatGptResponse res = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                Collections.singletonList(messageDto),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
        return res.getChoices().get(0).getMessage().getContent().replaceAll("/", "");
    }
}
