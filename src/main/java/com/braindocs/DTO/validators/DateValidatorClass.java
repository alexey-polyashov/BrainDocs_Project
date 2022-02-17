package com.braindocs.DTO.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidatorClass implements ConstraintValidator<DateValidator, String> {

    @Override
    public void initialize(DateValidator dateValidator) {

    }

    @Override
    public boolean isValid(String checkValue, ConstraintValidatorContext ctx) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        format.setLenient(false);
        try {
            format.parse(checkValue);
            return true;
        }catch (ParseException e) {
            return false;
        }
    }

}
