package com.braindocs.braindocs.repositories;

import com.braindocs.braindocs.models.documents.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentModel, Long>, JpaSpecificationExecutor<DocumentModel> {

}
