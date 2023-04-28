package com.example.filemaneger.file.reopsitory;


import com.example.filemaneger.file.domain.FilesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileRepository extends JpaRepository<FilesEntity, String> {

    FilesEntity findByUuidName(String uuid);

    public List<FilesEntity> findByFileName(String fileName);

    public Page<FilesEntity> findByFileNameLike(String fileName, Pageable pageable);

}
