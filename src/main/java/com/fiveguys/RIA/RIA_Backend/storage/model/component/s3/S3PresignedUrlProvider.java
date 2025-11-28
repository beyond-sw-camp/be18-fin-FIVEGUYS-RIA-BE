package com.fiveguys.RIA.RIA_Backend.storage.model.component.s3;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3PresignedUrlProvider {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String createPutUrl(String s3Key, String contentType, Duration duration) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(bucket)
                                                            .key(s3Key)
                                                            .contentType(contentType)
                                                            .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                                                                        .putObjectRequest(putObjectRequest)
                                                                        .signatureDuration(duration)
                                                                        .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);
        return presigned.url().toString();
    }

    public String createGetUrl(String s3Key, String originalFileName, Duration duration) {

        String disposition = buildAttachmentDisposition(originalFileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                            .bucket(bucket)
                                                            .key(s3Key)
                                                            .responseContentDisposition(disposition)
                                                            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                                                        .signatureDuration(duration)
                                                                        .getObjectRequest(getObjectRequest)
                                                                        .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);
        return presigned.url().toString();
    }

    private String buildAttachmentDisposition(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return "attachment";
        }

        try {
            String encoded = URLEncoder
                    .encode(originalName, StandardCharsets.UTF_8)
                    .replace("+", "%20");

            return "attachment; filename*=UTF-8''" + encoded;

        } catch (Exception e) {
            return "attachment";
        }
    }

}
