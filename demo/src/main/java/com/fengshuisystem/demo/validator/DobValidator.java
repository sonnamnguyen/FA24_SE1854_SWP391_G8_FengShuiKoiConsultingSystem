package com.fengshuisystem.demo.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    // minimum and maximum age
    private int min;
    private int max;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // trường ngày sinh bắt buộc
        if (Objects.isNull(value)) return false;

        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        // Kiểm tra khoảng ngày sinh hợp lệ
        return years >= min && years <= max;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }
}
