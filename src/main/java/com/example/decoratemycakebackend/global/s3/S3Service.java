package com.example.decoratemycakebackend.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.decoratemycakebackend.global.error.CustomException;
import com.example.decoratemycakebackend.global.error.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl;

    private final String dir = "/mycake";
    private final String profileDir = "/mycake/profile";

    @PostConstruct
    public void init() {
        this.defaultUrl = String.format("https://%s.s3.ap-northeast-2.amazonaws.com", bucketName);
    }

    public String uploadProfileImg(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }

        // 이미지 리사이즈
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = resizeImage(originalImage, 300, 300, contentType);

        // 이미지를 바이트 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, contentType.equals("image/png") ? "png" : "jpg", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        // S3 업로드
        String bucketDir = bucketName + profileDir;
        String dirUrl = defaultUrl + profileDir + "/";
        String fileName = generateFileName(file);

        amazonS3.putObject(bucketDir, fileName, inputStream, getObjectMetadata(imageBytes.length, contentType));
        return dirUrl + fileName;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight, String contentType) {
        int type = contentType.equals("image/png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double) targetWidth / originalImage.getWidth(),
                (double) targetHeight / originalImage.getHeight());
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        g.drawImage(originalImage, scaleOp, 0, 0);
        g.dispose();
        return resizedImage;
    }

    public String getImageUrl(String url) {
        return defaultUrl + dir + "/" + url + ".png";
    }

    private ObjectMetadata getObjectMetadata(long contentLength, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(contentLength);
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }
}
