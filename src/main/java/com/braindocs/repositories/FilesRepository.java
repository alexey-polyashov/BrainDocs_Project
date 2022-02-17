package com.braindocs.repositories;

import com.braindocs.models.files.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FileModel, Long> {

}
