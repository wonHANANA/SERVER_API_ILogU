package com.onehana.server_ilogu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "ILogU API 명세서",
                description = "ILogU",
                version = "v1")
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi user() {
        String[] paths = {"/api/user/**"};

        return GroupedOpenApi.builder()
                .group("유저")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi board() {
        String[] paths = {"/api/board/**"};

        return GroupedOpenApi.builder()
                .group("피드")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi family() {
        String[] paths = {"/api/family/**"};

        return GroupedOpenApi.builder()
                .group("가족")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi product() {
        String[] paths = {"/api/product/**"};

        return GroupedOpenApi.builder()
                .group("상품")
                .pathsToMatch(paths)
                .build();
    }
}
