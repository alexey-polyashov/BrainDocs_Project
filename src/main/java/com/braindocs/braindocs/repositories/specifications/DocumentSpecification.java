package com.braindocs.braindocs.repositories.specifications;
import com.braindocs.braindocs.exceptions.NotValidFields;
import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.exceptions.Violation;
import com.braindocs.braindocs.models.documents.DocumentModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DocumentSpecification implements Specification<DocumentModel> {

    private SearchCriteria criteria;
    private UserService userService;

    public DocumentSpecification(SearchCriteria criteria, UserService userService) {
        this.criteria = criteria;
        this.userService = userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
        }else if(valueClass == UserModel.class){
            value = userService.findById(Long.valueOf(value.toString()));
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
                        root.<String>get(criteria.getKey()), "%" + value + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), value);
            }
        }

        return null;
    }
}
