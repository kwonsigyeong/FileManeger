package com.example.filemaneger.controller;

import com.example.filemaneger.file.dto.FileUploadDTO;
import com.example.filemaneger.file.service.FileServiceByJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class FileController {

    private final FileServiceByJPA fileServiceByJPA;

    public FileController(FileServiceByJPA fileServiceByJPA) {
        this.fileServiceByJPA = fileServiceByJPA;
    }


    //단일 파일 업로드
    @PostMapping("/uploadFile")
    public FileUploadDTO uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileServiceByJPA.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();


        return new FileUploadDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<FileUploadDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // 파일을 Resource타입으로 받아온다.
        Resource resource = fileServiceByJPA.loadFileAsResource(fileName);
        // 파일 content type 확인 (jpg, png 등..)
        String encodedFileName = null;
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            encodedFileName = URLEncoder.encode(resource.getFilename(), "UTF-8").replaceAll("\\+", "%20");
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // 파일 타입을 알 수 없는 경우의 기본값
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();


        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/removeFile")
    public String removeFile(Model model,
                             @RequestParam(value = "deleteFile") String deleteFile)
            throws UnsupportedEncodingException {

        String srcFileName = URLDecoder.decode(deleteFile,"UTF-8");
        Path filePath = Paths.get(srcFileName);


        try {
            Files.delete(filePath);
        }catch (DirectoryNotEmptyException e) {
            System.out.println("디렉토리가 비어있지 않습니다");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "pages/delete";
    }

}
