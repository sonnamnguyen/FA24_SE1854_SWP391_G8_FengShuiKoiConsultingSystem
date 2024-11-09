package com.fengshuisystem.demo.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearOfBirthValidator.class)
public @interface ValidYearOfBirth {
    String message() default "Year of birth must be between 1900 and the current year.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

