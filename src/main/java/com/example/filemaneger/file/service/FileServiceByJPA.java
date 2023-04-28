package com.example.filemaneger.file.service;

import com.example.filemaneger.file.domain.FilesEntity;
import com.example.filemaneger.file.exception.FileNotFoundException;
import com.example.filemaneger.file.exception.FileStorageException;
import com.example.filemaneger.file.reopsitory.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service(value = "FileService")
@Slf4j
public class FileServiceByJPA implements FileService{

    private final Environment env;
    private final Path fileLocation;

    @Autowired
    private final FileRepository fileRepository;


    @Autowired
    public FileServiceByJPA(Environment env, FileRepository fileRepository) {
        this.env = env;
        this.fileLocation = Paths.get(env.getProperty("file.uploadDir")).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;

        try {
            Files.createDirectories(this.fileLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String storeFile(MultipartFile file) {
        // cleanPath : 역슬래시를 /슬래시로 바꿔줌
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // 저장할 fileStorageLocation 경로 뒤에 파일명을 붙여준다. (경로 조합)
            Path targetLocation = this.fileLocation.resolve(fileName);
            //업로드할 file을 targetLocation에 복사한다. (동일한 이름일 경우 replace)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            FilesEntity fileEntity = FilesEntity.builder()
                    .uuidName(UUID.randomUUID().toString())
                    .fileSize(file.getSize())
                    .filePath(targetLocation.toString())
                    .fileName(FilenameUtils.removeExtension(fileName))
                    .fileExtension(FilenameUtils.getExtension(fileName))
                    .fileType(file.getContentType())
                    .downloadLink("http://localhost:8081/downloadFile/" + fileName)
                    .build();

            fileRepository.save(fileEntity);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }



    public Resource loadFileAsResource(String fileName) {

        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }

    }

    @Override
    public List<FilesEntity> getList() {
        return fileRepository.findAll(Sort.by(Sort.Direction.ASC, "fileName"));
    }

    @Override
    public Page<FilesEntity> getPagingList(int pageNumber) {
        return fileRepository.findAll(PageRequest.of(pageNumber, 30));
    }

    @Override
    public Page<FilesEntity> getSearchList(String search, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 30,
                Sort.by(Sort.Direction.ASC, "fileName"));

        return fileRepository.findByFileNameLike("%" + search + "%", pageable);
    }

}
