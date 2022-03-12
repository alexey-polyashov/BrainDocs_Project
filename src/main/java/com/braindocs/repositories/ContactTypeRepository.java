package com.braindocs.repositories;

import com.braindocs.models.ContactTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactTypeModel, Long> {
    List<ContactTypeModel> findByMarked(boolean b);
}
