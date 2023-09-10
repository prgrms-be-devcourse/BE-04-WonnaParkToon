package com.wonnapark.wnpserver.media;

import com.amazonaws.services.s3.AmazonS3;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class S3ManageService {

    private final AmazonS3 amazonS3;

    private final String bucketName;

    public S3ManageService(
            AmazonS3 amazonS3,
            @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public void upload(String key, MultipartFile file) throws IOException {
        amazonS3.putObject(bucketName, key, FileUtils.convertMultipartFileToFile(file));
    }

    public void upload(String key, File file) {
        amazonS3.putObject(bucketName, key, file);
    }

    public void delete(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public String findObjectUrlByName(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }

}
