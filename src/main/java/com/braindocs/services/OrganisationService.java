package com.braindocs.services;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.common.Utils;
import com.braindocs.dto.SearchCriteriaDTO;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.organisations.OrganisationContactsModel;
import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.repositories.organisation.OrganisationContactRepository;
import com.braindocs.repositories.organisation.OrganisationRepository;
import com.braindocs.repositories.specifications.OrganisationSpecificationBuilder;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final OrganisationContactRepository orgContactsRepository;
    private final Options options;
    private final UserService userService;

    public OrganisationModel findById(Long id){
        return organisationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Организаия '" + id + "' не найдена"));
    }

    public Long add(OrganisationModel organisation) {
        OrganisationModel newOrganisation = organisationRepository.save(organisation);
        return newOrganisation.getId();
    }

    @Transactional
    public Long change(Long orgid, OrganisationModel organisation) {
        OrganisationModel oldOrganisation = organisationRepository.findById(orgid).orElseThrow(()->new ResourceNotFoundException("Организаия с id '" + orgid + "' не найдена"));
        oldOrganisation.setName(organisation.getName());
        oldOrganisation.setInn(organisation.getInn());
        oldOrganisation.setKpp(organisation.getKpp());
        List<OrganisationContactsModel> orgContacts = organisation.getContacts();
        orgContactsRepository.deleteByOrganisation(orgid);
        if(orgContacts!=null){
            for (OrganisationContactsModel orgContact: orgContacts) {
                orgContactsRepository.save(orgContact);
            }
        }
        return oldOrganisation.getId();
    }

    public Page<OrganisationModel> getOrganisationByFields(Integer page, Integer recordsOnPage, List<SearchCriteriaDTO> filter) {

        List<SearchCriteriaDTO> markedCriteria = filter.stream()
                .filter(p->p.getKey().equals("marked"))
                .collect(Collectors.toList());

        if(markedCriteria.isEmpty()){
            filter.add(new SearchCriteriaDTO("marked", ":", "OFF"));
        }else{
            if(!Utils.isValidEnum(MarkedRequestValue.class,
                    markedCriteria.get(0)
                            .getValue()
                            .toUpperCase(Locale.ROOT))){
                throw new BadRequestException("Недопустимое значение параметра marked");
            }
        }

        OrganisationSpecificationBuilder builder =
                new OrganisationSpecificationBuilder(
                        userService,
                        options);
        for(SearchCriteriaDTO creteriaDTO: filter) {
            Object value = creteriaDTO.getValue();
            builder.with(creteriaDTO.getKey(), creteriaDTO.getOperation(), value);
        }
        Specification<OrganisationModel> spec = builder.build();

        return organisationRepository.findAll(spec, PageRequest.of(page, recordsOnPage));
    }

    public void deleteById(Long orgid) {
        organisationRepository.deleteById(orgid);
    }

    public void setMark(Long orgid, Boolean mark) {
        OrganisationModel org = organisationRepository.findById(orgid)
                .orElseThrow(()->new ResourceNotFoundException("Организация с id '" + orgid + "' не найдена"));
        org.setMarked(mark);
        organisationRepository.save(org);
    }

    public List<OrganisationModel> findAll(MarkedRequestValue marked) {
        switch(marked){
            case OFF:
                return organisationRepository.findByMarked(false);
            case ONLY:
                return organisationRepository.findByMarked(true);
            default:
                return organisationRepository.findAll();
        }
    }
}
