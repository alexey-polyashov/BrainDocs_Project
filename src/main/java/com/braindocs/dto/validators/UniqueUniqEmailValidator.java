package com.braindocs.dto.validators;

import com.braindocs.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUniqEmailValidator implements ConstraintValidator<UniqUserEmail, String> {


    @Autowired
    private UserService usersService;

    @Override
    public void initialize(UniqUserEmail uniqUserEmail) {

    }

    @Override
    public boolean isValid(String checkValue, ConstraintValidatorContext ctx) {
        return !usersService.findByEmail(checkValue).isPresent();
    }

}
