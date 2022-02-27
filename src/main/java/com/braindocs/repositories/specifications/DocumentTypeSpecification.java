package com.braindocs.repositories.specifications;

import com.braindocs.exceptions.NotValidFields;
import com.braindocs.exceptions.Violation;
import com.braindocs.models.documents.DocumentModel;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Data
public class DocumentTypeSpecification implements Specification<DocumentModel> {

    private SearchCriteria criteria;

    public DocumentTypeSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate
            (Root<DocumentModel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Class valueClass = root.get(criteria.getKey()).getJavaType();
        Object value = criteria.getValue();
        if(valueClass == java.sql.Date.class){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                value = new Date(sdf.parse(value.toString()).getTime());
            } catch (ParseException e) {
                throw new NotValidFields(Arrays.asList(new Violation(criteria.getKey(), "Не корректный формат даты")));
            }
        }

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if(value instanceof Date) {
                return builder.greaterThanOrEqualTo(
                        root.<Date>get(criteria.getKey()), (Date) value);
            }else{
                return builder.greaterThanOrEqualTo(
                        root.<String>get(criteria.getKey()), value.toString());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if(value instanceof Date) {
                return builder.lessThanOrEqualTo(
                        root.<Date>get(criteria.getKey()), (Date) value);
            }else{
                return builder.lessThanOrEqualTo(
                        root.<String>get(criteria.getKey()), value.toString());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        builder.lower(root.<String>get(criteria.getKey())),
                        "%" + value + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), value);
            }
        }

        return null;
    }
}