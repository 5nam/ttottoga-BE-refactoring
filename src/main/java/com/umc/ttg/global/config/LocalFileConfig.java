package com.umc.ttg.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

//@Configuration
public class LocalFileConfig {

    @Value("${local.upload.directory}")
    private String fileUploadDir;

    @Bean
    public File file() {

        File directory = new File(fileUploadDir);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }
}
