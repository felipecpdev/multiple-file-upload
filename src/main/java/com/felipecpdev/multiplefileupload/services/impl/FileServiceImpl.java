package com.felipecpdev.multiplefileupload.services.impl;

import com.felipecpdev.multiplefileupload.exception.ExtensionErrorException;
import com.felipecpdev.multiplefileupload.services.FileServiceAPI;
import com.felipecpdev.multiplefileupload.services.utils.Util;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileServiceAPI {

    private final Path rootFolder = Paths.get("uploads");

    private final Util utils;

    public FileServiceImpl(Util utils) {
        this.utils = utils;
    }

    @Override
    public void save(MultipartFile multipartFile) throws Exception {
        Files.copy(multipartFile.getInputStream(),
                this.rootFolder.resolve(utils.buildFileName(multipartFile)));
    }

    @Override
    public Resource load(String name) throws Exception {
        Path file = rootFolder.resolve(name);
        Resource resource = new UrlResource(file.toUri());
        return resource;
    }

    @Override
    public void save(List<MultipartFile> multipartFileList) throws Exception {
        for (MultipartFile file : multipartFileList) {
            if (utils.isPngOrJpgFile(file)) {
                // Process the file as PNG or JPEG
                this.save(file);
            } else {
                // File is not PNG or JPEG, handle the error or message
                throw new ExtensionErrorException("Please upload a PNG or JPEG file!");
            }
        }
    }

    @Override
    public Stream<Path> loadAll() throws Exception {
        return Files.walk(rootFolder, 1)
                .filter(path -> !path.equals(rootFolder))
                .map(rootFolder::relativize);
    }
}
