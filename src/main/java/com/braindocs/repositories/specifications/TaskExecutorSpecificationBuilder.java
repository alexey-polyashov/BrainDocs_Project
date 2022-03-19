package com.braindocs.repositories.specifications;

import com.braindocs.common.Options;
import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.services.OrganisationService;
import com.braindocs.services.tasks.TaskTypesService;
import com.braindocs.services.users.UserService;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TaskExecutorSpecificationBuilder {

    private final List<SearchCriteria> params;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final TaskTypesService taskTypesService;
    private final Options options;

    public TaskExecutorSpecificationBuilder(UserService userService,
                                            OrganisationService organisationService,
                                            TaskTypesService taskTypesService,
                                            Options options) {
        params = new ArrayList<SearchCriteria>();
        this.userService = userService;
        this.organisationService = organisationService;
        this.taskTypesService = taskTypesService;
        this.options = options;
    }

    public TaskExecutorSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<TaskExecutorModel> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(p -> new TaskExecutorSpecification(p, userService, organisationService, taskTypesService, options))
                .collect(Collectors.toList());

        Specification<TaskExecutorModel> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }

}
