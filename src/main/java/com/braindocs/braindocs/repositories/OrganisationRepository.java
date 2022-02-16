package com.braindocs.braindocs.repositories;

import com.braindocs.braindocs.models.organisations.OrganisationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<OrganisationModel, Long> {
}
