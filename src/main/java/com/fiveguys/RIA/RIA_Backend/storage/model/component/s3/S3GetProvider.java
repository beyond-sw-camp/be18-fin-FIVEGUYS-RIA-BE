package com.fiveguys.RIA.RIA_Backend.storage.model.component.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class S3GetProvider {

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

    public String createGetUrl(String s3Key, Duration duration) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                            .bucket(bucket)
                                                            .key(s3Key)
                                                            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                                                        .signatureDuration(duration)
                                                                        .getObjectRequest(getObjectRequest)
                                                                        .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);

        return presigned.url().toString();
    }
}
