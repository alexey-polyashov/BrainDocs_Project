package com.braindocs.repositories.organisation;

import com.braindocs.models.organisations.OrganisationContactsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationContactRepository extends JpaRepository<OrganisationContactsModel, Long> {

    void deleteByOrganisation(Long organisation);

}
