package com.braindocs.services;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.repositories.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationModel findById(Long id){
        return organisationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Организаия '" + id + "' не найдена"));
    }
}
