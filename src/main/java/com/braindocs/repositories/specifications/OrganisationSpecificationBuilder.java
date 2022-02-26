package com.braindocs.repositories.specifications;

import com.braindocs.models.organisations.OrganisationModel;
import com.braindocs.services.UserService;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class OrganisationSpecificationBuilder {

    private final List<SearchCriteria> params;
    private UserService userService;

    public OrganisationSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public OrganisationSpecificationBuilder(UserService userService) {
        params = new ArrayList<SearchCriteria>();
        this.userService = userService;
    }

    public Specification<OrganisationModel> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(p->new OrganisationSpecification(p, userService))
                .collect(Collectors.toList());

        Specification<OrganisationModel> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }

}
