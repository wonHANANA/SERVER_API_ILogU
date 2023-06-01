package com.onehana.server_ilogu.config;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${cloud.azure.computer-vision.endpoint}")
    private String endpoint;

    @Value("${cloud.azure.computer-vision.subscription-key}")
    private String subscriptionKey;

    @Bean
    public ComputerVisionClient computerVisionClient() {
        return ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);
    }
}
