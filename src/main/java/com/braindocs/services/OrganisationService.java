package com.braindocs.services;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.repositories.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationModel findById(Long id){
        return organisationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Организаия '" + id + "' не найдена"));
    }

    public Long add(OrganisationModel organisation) {
        OrganisationModel newOrganisation = organisationRepository.save(organisation);
        return newOrganisation.getId();
    }

    public Long change(Long orgid, OrganisationModel organisation) {
        OrganisationModel oldOrganisation = organisationRepository.findById(orgid).orElseThrow(()->new ResourceNotFoundException("Организаия с id '" + orgid + "' не найдена"));
        oldOrganisation.setName(organisation.getName());
        oldOrganisation.setInn(organisation.getInn());
        oldOrganisation.setKpp(organisation.getKpp());
        organisationRepository.save(oldOrganisation);
        return oldOrganisation.getId();    }

    public Page<OrganisationModel> getOrganisationByFields(Integer page, Integer recordsOnPage, Specification<OrganisationModel> spec) {
        return organisationRepository.findAll(spec, PageRequest.of(page, recordsOnPage));
    }

    public void deleteById(Long orgid) {
        organisationRepository.deleteById(orgid);
    }

    public void markById(Long orgid) {
        OrganisationModel org = organisationRepository.findById(orgid)
                .orElseThrow(()->new ResourceNotFoundException("Организация с id '" + orgid + "' не найдена"));
        org.setMarked(true);
        organisationRepository.save(org);
    }

    public List<OrganisationModel> findAll() {
        return organisationRepository.findAll();
    }
}
