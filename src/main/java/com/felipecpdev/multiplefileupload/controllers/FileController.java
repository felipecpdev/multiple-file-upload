package com.felipecpdev.multiplefileupload.controllers;

import com.felipecpdev.multiplefileupload.services.FileServiceAPI;
import com.felipecpdev.multiplefileupload.services.utils.File;
import com.felipecpdev.multiplefileupload.services.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    @Autowired
    private FileServiceAPI fileServiceAPI;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") List<MultipartFile> fileList) {
        try {
            fileServiceAPI.save(fileList);
            return new ResponseEntity<>(new ResponseMessage("Files were uploaded successfully",
                    HttpStatus.OK.value(), LocalDateTime.now()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("An error occurred while uploading files: " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws Exception {
        Resource resource = fileServiceAPI.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION
                        , "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<File>> getAllFiles() throws Exception {
        List<File> files = fileServiceAPI.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(
                            FileController.class,
                            "getFile",
                            path.getFileName().toString())
                    .build()
                    .toString();
            return new File(filename, url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
}
