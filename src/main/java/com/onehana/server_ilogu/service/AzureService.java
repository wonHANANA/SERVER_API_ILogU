package com.onehana.server_ilogu.service;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageTag;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AzureService {

    private final ComputerVisionClient client;

    public String analyzeImage(byte[] imageData) {
        ImageAnalysis analysis = client.computerVision().analyzeImageInStream()
                .withImage(imageData)
                .withVisualFeatures(
                        new ArrayList<>() {{
                            add(VisualFeatureTypes.DESCRIPTION);
                            add(VisualFeatureTypes.CATEGORIES);
                            add(VisualFeatureTypes.TAGS);
                        }}
                )
                .execute();

        List<ImageTag> tags = analysis.tags();
        String description = analysis.description().captions().get(0).text();

        System.out.println("이미지 요약 결과 :" + description);
        for (ImageTag tag : tags) {
            System.out.print(tag.name() + " ");
            System.out.println();
        }

        String joinedString = tags.stream()
                .map(ImageTag::name)
                .collect(Collectors.joining(","));

        joinedString = description + "," + joinedString;
        return joinedString;
    }
}
