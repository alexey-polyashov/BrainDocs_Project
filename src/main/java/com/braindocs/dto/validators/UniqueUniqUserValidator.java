package com.braindocs.dto.validators;

import com.braindocs.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUniqUserValidator implements ConstraintValidator<UniqUserName, String> {


    @Autowired
    private UserService usersService;

    @Override
    public void initialize(UniqUserName uniqUserName) {

    }

    @Override
    public boolean isValid(String checkValue, ConstraintValidatorContext ctx) {
        return usersService.findByLogin(checkValue).isPresent();
    }

}
