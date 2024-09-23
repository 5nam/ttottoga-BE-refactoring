package com.umc.ttg.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileService {
    String upload(MultipartFile multipartFile, String directoryName) throws IOException;
    void remove(String path);
}
