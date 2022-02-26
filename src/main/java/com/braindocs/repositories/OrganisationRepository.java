package com.braindocs.repositories;

import com.braindocs.models.organisations.OrganisationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<OrganisationModel, Long>,
        JpaSpecificationExecutor<OrganisationModel> {
}
