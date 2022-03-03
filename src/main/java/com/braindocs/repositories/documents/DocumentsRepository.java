package com.braindocs.repositories.documents;

import com.braindocs.models.documents.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentModel, Long>,
        JpaSpecificationExecutor<DocumentModel> {

    Page<DocumentModel> findByMarked(Boolean marked, PageRequest of);

}
