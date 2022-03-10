package com.braindocs.repositories.documents;

import com.braindocs.models.documents.DocumentTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypeModel, Long>
        , JpaSpecificationExecutor<DocumentTypeModel> {

    List<DocumentTypeModel> findByMarked(Boolean marked);

}
