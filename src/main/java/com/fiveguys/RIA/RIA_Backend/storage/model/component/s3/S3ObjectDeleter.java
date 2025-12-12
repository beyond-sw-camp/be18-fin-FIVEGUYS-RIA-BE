package com.fiveguys.RIA.RIA_Backend.storage.model.component.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ObjectDeleter {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void deleteObject(String s3Key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                                                             .bucket(bucket)
                                                             .key(s3Key)
                                                             .build();

            s3Client.deleteObject(request);
            log.info("[S3-DELETE] bucket={}, key={}", bucket, s3Key);

        } catch (S3Exception e) {
            log.error("[S3-DELETE-ERROR] bucket={}, key={}, message={}",
                      bucket, s3Key, e.getMessage(), e);
            throw e;
        }
    }
}
