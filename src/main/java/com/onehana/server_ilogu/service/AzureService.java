package com.onehana.server_ilogu.service;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*;
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

        String joinedString = tags.stream()
                .map(ImageTag::name)
                .collect(Collectors.joining(","));

        joinedString = description + "," + joinedString;
        return joinedString;
    }

    public String analyzeText(byte[] imageData) {
        OcrResult result = client.computerVision().recognizePrintedTextInStream()
                .withDetectOrientation(true)
                .withImage(imageData)
                .execute();

        return result.regions().stream()
                .flatMap(region -> region.lines().stream())
                .flatMap(line -> line.words().stream())
                .map(OcrWord::text)
                .collect(Collectors.joining(" "));
    }
}
