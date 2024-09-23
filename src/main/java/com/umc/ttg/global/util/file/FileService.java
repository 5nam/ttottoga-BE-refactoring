package com.umc.ttg.global.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String upload(MultipartFile multipartFile, String directoryName) throws IOException;
    void remove(String path);
}
