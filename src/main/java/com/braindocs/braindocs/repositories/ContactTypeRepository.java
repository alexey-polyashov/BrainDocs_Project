package com.braindocs.braindocs.repositories;

import com.braindocs.braindocs.models.ContactTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactTypeModel, Long> {
}
