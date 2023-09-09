package com.wonnapark.wnpserver.media;

import com.amazonaws.services.s3.AmazonS3;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3MediaService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3MediaService(
            AmazonS3 amazonS3,
            @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String upload(String key, MultipartFile file) {
        amazonS3.putObject(bucketName, key, FileUtils.convertMultipartFileToFile(file));
        return amazonS3.getUrl(bucketName, key).toExternalForm();
    }

    public void delete(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

}
