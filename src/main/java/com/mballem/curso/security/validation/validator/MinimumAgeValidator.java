package com.mballem.curso.security.validation.validator;

import java.time.LocalDate;
import java.time.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.mballem.curso.security.validation.MinimumAge;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {

	private int minValue;

	@Override
	public void initialize(MinimumAge constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);

		minValue = constraintAnnotation.min();
	}

	@Override
	public boolean isValid(LocalDate valueDate, ConstraintValidatorContext context) {
		LocalDate currentDate = LocalDate.now();

		if(valueDate == null) {
			return false;
		}
		
		if(valueDate.isAfter(currentDate)) {
			return false;
		}
		
		Period intervalPeriod = Period.between(valueDate, currentDate);

		return (intervalPeriod.getYears() >= minValue);

	}

}
