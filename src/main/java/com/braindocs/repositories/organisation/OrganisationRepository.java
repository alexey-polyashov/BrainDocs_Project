package com.braindocs.repositories.organisation;

import com.braindocs.models.organisations.OrganisationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<OrganisationModel, Long>,
        JpaSpecificationExecutor<OrganisationModel> {
    List<OrganisationModel> findByMarked(boolean b);
}
