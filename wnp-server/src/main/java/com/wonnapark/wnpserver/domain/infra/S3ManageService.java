package com.wonnapark.wnpserver.domain.infra;

import com.amazonaws.services.s3.AmazonS3;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3ManageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void upload(String key, MultipartFile file) throws IOException {
        amazonS3.putObject(bucketName, key, FileUtils.convertMultipartFileToFile(file));
    }

    public void upload(String key, File file) {
        amazonS3.putObject(bucketName, key, file);
    }

    public void delete(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

}
