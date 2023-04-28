package com.example.filemaneger.file.service;

import com.example.filemaneger.file.domain.FilesEntity;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    public String storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);

    public List<FilesEntity> getList();

    public Page<FilesEntity> getPagingList(int pageNumber);

    public Page<FilesEntity> getSearchList(String search, int pageNumber);


}
