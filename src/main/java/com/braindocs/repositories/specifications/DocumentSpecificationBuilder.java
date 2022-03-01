package com.braindocs.repositories.specifications;

import com.braindocs.models.documents.DocumentModel;
import com.braindocs.services.documents.DocumentTypeService;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.users.UserService;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DocumentSpecificationBuilder {

    private final List<SearchCriteria> params;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final DocumentTypeService documentTypeService;

    public DocumentSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public DocumentSpecificationBuilder(UserService userService,
                                        OrganisationService organisationService,
                                        DocumentTypeService documentTypeService) {
        params = new ArrayList<SearchCriteria>();
        this.userService = userService;
        this.organisationService = organisationService;
        this.documentTypeService = documentTypeService;
    }

    public Specification<DocumentModel> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map((p)->new DocumentSpecification(p, userService, organisationService, documentTypeService))
                .collect(Collectors.toList());

        Specification<DocumentModel> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }

}
