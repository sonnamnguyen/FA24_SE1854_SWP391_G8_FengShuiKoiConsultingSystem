package com.fengshuisystem.demo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearOfBirthValidator implements ConstraintValidator<ValidYearOfBirth, Integer> {

    @Override
    public boolean isValid(Integer yearOfBirth, ConstraintValidatorContext context) {
        if (yearOfBirth == null) {
            return false; // or true, depending on whether you want null to be valid
        }
        int currentYear = Year.now().getValue();
        return yearOfBirth >= 1900 && yearOfBirth <= currentYear;
    }
}
