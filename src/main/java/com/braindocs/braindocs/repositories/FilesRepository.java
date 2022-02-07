package com.braindocs.braindocs.repositories;

import com.braindocs.braindocs.models.files.FilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FilesModel, Long> {

}
