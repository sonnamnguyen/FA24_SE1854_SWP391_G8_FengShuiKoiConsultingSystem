package com.fengshuisystem.demo.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    // tuổi tối thiểu và tối đa cho phép
    int min() default 1;
    int max() default 150;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}