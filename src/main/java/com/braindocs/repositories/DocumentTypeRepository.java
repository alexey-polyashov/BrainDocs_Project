package com.braindocs.repositories;

import com.braindocs.models.documents.DocumentTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypeModel, Long> {

}
