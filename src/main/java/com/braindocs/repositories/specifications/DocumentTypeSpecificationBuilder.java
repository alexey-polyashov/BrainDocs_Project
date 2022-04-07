package com.braindocs.repositories.specifications;

import com.braindocs.common.Options;
import com.braindocs.models.documents.DocumentTypeModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DocumentTypeSpecificationBuilder {

    private final List<SearchCriteria> params;
    private final Options options;

    public DocumentTypeSpecificationBuilder(Options options) {
        this.options = options;
        params = new ArrayList<SearchCriteria>();
    }

    public DocumentTypeSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<DocumentTypeModel> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(p -> new DocumentTypeSpecification(p, options))
                .collect(Collectors.toList());

        Specification<DocumentTypeModel> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }

}
