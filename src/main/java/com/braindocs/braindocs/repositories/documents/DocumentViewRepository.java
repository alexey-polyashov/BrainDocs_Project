package com.braindocs.braindocs.repositories.documents;


import com.braindocs.braindocs.models.documents.DocumentViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentViewRepository extends JpaRepository<DocumentViewModel,Long> {

}
