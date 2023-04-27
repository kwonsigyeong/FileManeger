package com.example.filemaneger.file.reopsitory;


import com.example.filemaneger.file.domain.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<FilesEntity, String> {

    FilesEntity findByUuidName(String uuid);

}
