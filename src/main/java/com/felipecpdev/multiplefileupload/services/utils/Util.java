package com.felipecpdev.multiplefileupload.services.utils;

import com.felipecpdev.multiplefileupload.exception.EmptyFileException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Configuration
public class Util {

    public boolean isPngOrJpgFile(MultipartFile file) {
        List<String> allowedTypes = List.of("image/png", "image/jpeg");

        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            return allowedTypes.contains(contentType);
        }
        throw new EmptyFileException("Select a file is empty!");
    }

    public String getFileExtension(MultipartFile file) {
        if (!file.isEmpty()) {
            Path path = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
            return path.getFileName().toString().contains(".") ?
                    path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf('.') + 1) :
                    "";
        }
        return "";
    }

    public String buildFileName(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String epochMilli = String.valueOf(System.currentTimeMillis());
        String extension = getFileExtension(file);
        return uuid + "-" + epochMilli + "." + extension;
    }

}
