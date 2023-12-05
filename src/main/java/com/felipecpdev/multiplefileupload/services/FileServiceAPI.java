package com.felipecpdev.multiplefileupload.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileServiceAPI {

    public void save(MultipartFile multipartFile) throws Exception;

    public Resource load(String name) throws Exception;

    public void save(List<MultipartFile> multipartFileList) throws Exception;

    public Stream<Path> loadAll() throws Exception;

}
