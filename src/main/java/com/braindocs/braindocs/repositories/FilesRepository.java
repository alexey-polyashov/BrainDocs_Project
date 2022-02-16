package com.braindocs.braindocs.repositories;

import com.braindocs.braindocs.models.files.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FileModel, Long> {

}
