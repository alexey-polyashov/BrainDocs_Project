package com.braindocs.braindocs.repositories.documents;


import com.braindocs.braindocs.models.documents.DocumentStorageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStorageRepository extends JpaRepository<DocumentStorageModel,Long> {

}
