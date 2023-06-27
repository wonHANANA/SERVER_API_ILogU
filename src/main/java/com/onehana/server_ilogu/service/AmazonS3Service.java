package com.onehana.server_ilogu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onehana.server_ilogu.dto.BoardDto;
import com.onehana.server_ilogu.dto.BoardImageDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.Board;
import com.onehana.server_ilogu.entity.BoardImage;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.BoardImageRepository;
import com.onehana.server_ilogu.repository.BoardRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final AmazonS3Client amazonS3Client;

    public List<BoardImageDto> uploadBoardImages(List<MultipartFile> files, BoardDto boardDto) {
        List<BoardImageDto> s3files = new ArrayList<>();
        String uploadFilePath = "board" + "/" + getFolderName();

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String uploadFileName = getUuidFileName(Objects.requireNonNull(file.getOriginalFilename()));
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                String keyName = uploadFilePath + "/" + uploadFileName;

                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));

                uploadFileUrl = amazonS3Client.getUrl(bucket, keyName).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s3files.add(
                    BoardImageDto.builder()
                            .originalFileName(originalFileName)
                            .uploadFileName(uploadFileName)
                            .uploadFilePath(uploadFilePath)
                            .uploadFileUrl(uploadFileUrl)
                            .build());
        }
        Board board = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        List<BoardImage> boardImages = s3files.stream()
                .map(dto -> BoardImage.from(dto, board))
                .collect(Collectors.toList());

        boardImageRepository.saveAll(boardImages);
        return s3files;
    }

    public String uploadProfileImage(MultipartFile file) {
        String uploadFilePath = "profile" + "/" + getFolderName();
        String uploadFileName = getUuidFileName(Objects.requireNonNull(file.getOriginalFilename()));
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            String keyName = uploadFilePath + "/" + uploadFileName;

            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));

            uploadFileUrl = amazonS3Client.getUrl(bucket, keyName).toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFileUrl;
    }

    public void deleteAllBoardImages(String email, Long boardId) {
        User user = getUserOrException(email);
        Board board = getBoardOrException(boardId);

        if (board.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }

        List<BoardImage> boardImages = boardImageRepository.findByBoardId(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

        for (BoardImage boardImage : boardImages) {
            String keyName = boardImage.getUploadFilePath() + "/" + boardImage.getUploadFileName();
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);

            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, keyName);
            }
        }
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }

    private String getFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }

    private User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }

    private Board getBoardOrException(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
    }
}
