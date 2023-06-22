package com.onehana.server_ilogu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
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

    public OpenApiCustomizer buildSecurityOpenApi() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .getComponents().addSecuritySchemes("jwt token", securityScheme);
    }

    @Bean
    public GroupedOpenApi user() {
        String[] paths = {"/api/user/**"};

        return GroupedOpenApi.builder()
                .group("유저")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi board() {
        String[] paths = {"/api/board/**"};

        return GroupedOpenApi.builder()
                .group("피드")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi family() {
        String[] paths = {"/api/family/**"};

        return GroupedOpenApi.builder()
                .group("가족")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi product() {
        String[] paths = {"/api/products/**"};

        return GroupedOpenApi.builder()
                .group("금융 상품")
                .pathsToMatch(paths)
                .build();
    }
}
