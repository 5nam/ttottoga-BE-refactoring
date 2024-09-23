package com.umc.ttg.global.util;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.handler.LocalFileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileService implements FileService {

    @Value("${local.upload.directory}")
    private String fileUploadDir;
    private final static int FILE_MAX_SIZE = 104_857_600; // 100MB

    @Override
    public String upload(MultipartFile multipartFile, String directoryName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String mimeType = multipartFile.getContentType();

        // 최대 용량 체크
        if (multipartFile.getSize() > FILE_MAX_SIZE) {
            throw new LocalFileHandler(ResponseCode.FILE_MAX_SIZE_OVER);
        }

        // MIMETYPE 체크
        if (!fileTypeCheck(mimeType)) {
            throw new LocalFileHandler(ResponseCode.FILE_CONTENT_TYPE_NOT_IMAGE);
        }

        String randomFileName = directoryName + "/" + UUID.randomUUID() + originalFileName;
        File file = new File(fileUploadDir + "/" + randomFileName);

        // file 저장
        if (file.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            } catch (Exception e) {
                throw new LocalFileHandler(ResponseCode.FILE_SAVE_FAIL);
            }
        }

        return file.getPath();
    }

    private boolean fileTypeCheck(String mimeType) {
        return mimeType.startsWith("image");
    }

    @Override
    public void remove(String path) {
        File file = new File(path);
        file.delete();
    }
}
