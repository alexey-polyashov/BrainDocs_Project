package com.braindocs.braindocs.DTO.users;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUniqUserValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqUserName {

    String message() default "Логин не уникален";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
