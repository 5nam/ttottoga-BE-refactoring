package com.umc.ttg.global.util.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.ttg.global.common.AwsS3;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.handler.AwsS3Handler;
import com.umc.ttg.global.util.uuid.SystemUuidHolder;
import com.umc.ttg.global.util.uuid.UuidHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

//@Service
@RequiredArgsConstructor
public class AwsS3FileService implements FileService {

    private final AmazonS3 amazonS3;
    private final UuidHolder uuidHolder;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String directoryName) throws IOException {
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new AwsS3Handler(ResponseCode.S3_UPLOAD_FAIL));

        return upload(file, directoryName).getPath();
    }

    private AwsS3 upload(File file, String directoryName) {
        String key = randomFileName(file, directoryName);
        String path = putS3(file, key);

        removeFile(file);

        return AwsS3
                .builder()
                .key(key)
                .path(path)
                .build();
    }

    private String randomFileName(File file, String directoryName) {
        return directoryName + "/" + uuidHolder.randomUuid() + file.getName();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getS3(bucket, fileName);
    }

    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        file.delete();
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        if(file.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    public void remove(String path) {
        if (!amazonS3.doesObjectExist(bucket, path)) {
            throw new AmazonS3Exception("Object " + path + " does not exist!");
        }
        amazonS3.deleteObject(bucket, path);
    }
}
