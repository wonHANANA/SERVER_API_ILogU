package com.onehana.server_ilogu.service;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*;
import com.onehana.server_ilogu.dto.ImageAdultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AzureService {

    private final ComputerVisionClient client;

    public String analyzeImageForGPT(byte[] imageData) {
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

    public List<ImageAdultDto> analyzeImagesForAdult(List<MultipartFile> files) {
        List<ImageAdultDto> results = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                byte[] imageData = file.getBytes();
                String imageName = file.getOriginalFilename();
                ImageAdultDto isAdult = analyzeImageForAdult(imageData, imageName);
                results.add(isAdult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ImageAdultDto analyzeImageForAdult(byte[] imageData, String imageName) {
        ImageAnalysis analysis = client.computerVision().analyzeImageInStream()
                .withImage(imageData)
                .withVisualFeatures(List.of(VisualFeatureTypes.ADULT))
                .execute();

        boolean isAdult = analysis.adult().isAdultContent();
        boolean isGory = analysis.adult().isGoryContent();
        boolean isRacy = analysis.adult().isRacyContent();

        return ImageAdultDto.of(imageName, isAdult, isGory, isRacy);
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
