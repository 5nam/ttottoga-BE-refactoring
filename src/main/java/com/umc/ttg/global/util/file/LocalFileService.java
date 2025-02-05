package com.umc.ttg.global.util.file;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.handler.LocalFileHandler;
import com.umc.ttg.global.util.uuid.UuidHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("test")
public class LocalFileService implements FileService {

    @Value("${local.upload.directory}")
    private String fileUploadDir;
    private final static int FILE_MAX_SIZE = 104_857_600; // 100MB

    private final UuidHolder uuidHolder;

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

        String randomFileName = directoryName + "/" + uuidHolder.randomUuid() + originalFileName;
        File file = new File(fileUploadDir + "/" + randomFileName);

        File parentDir = file.getParentFile();
        if(!parentDir.exists() && !parentDir.mkdir()) {
            throw new LocalFileHandler(ResponseCode.FILE_SAVE_FAIL);
        }

        // file 저장
        log.info(String.valueOf(file.exists()));

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
