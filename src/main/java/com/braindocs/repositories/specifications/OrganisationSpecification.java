package com.braindocs.repositories.specifications;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.common.Options;
import com.braindocs.exceptions.NotValidFields;
import com.braindocs.exceptions.Violation;
import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.users.UserService;
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
import java.util.Locale;

@Data
public class OrganisationSpecification implements Specification<DocumentModel> {

    private final Options options;
    private SearchCriteria criteria;
    private UserService userService;

    public OrganisationSpecification(SearchCriteria criteria,
                                     UserService userService,
                                     Options options) {
        this.options = options;
        this.criteria = criteria;
        this.userService = userService;
    }

    @Override
    public Predicate toPredicate
            (Root<DocumentModel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Class valueClass = root.get(criteria.getKey()).getJavaType();
        Object value = criteria.getValue();

        if (valueClass == java.sql.Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                value = new Date(sdf.parse(value.toString()).getTime());
            } catch (ParseException e) {
                throw new NotValidFields(Arrays.asList(new Violation(criteria.getKey(), "Не корректный формат даты")));
            }
        } else if (valueClass == UserModel.class) {
            value = userService.findById(Long.valueOf(value.toString()));
        }

        return getPredicate(root, builder, value);
    }

    private Predicate getPredicate(Root<DocumentModel> root, CriteriaBuilder builder, Object value) {
        if (criteria.getKey().equals("marked")) {
            MarkedRequestValue marked = MarkedRequestValue.valueOf(
                    criteria.getValue().toString().toUpperCase(Locale.ROOT));
            if (marked == MarkedRequestValue.ONLY) {
                return builder.equal(root.get(criteria.getKey()), true);
            } else if (marked == MarkedRequestValue.OFF) {
                return builder.equal(root.get(criteria.getKey()), false);
            } else {
                return null;
            }
        } else if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (value instanceof Date) {
                return builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), (Date) value);
            } else {
                return builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), value.toString());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (value instanceof Date) {
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), (Date) value);
            } else {
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), value.toString());
            }
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + value + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), value);
            }
        }

        return null;
    }
}
