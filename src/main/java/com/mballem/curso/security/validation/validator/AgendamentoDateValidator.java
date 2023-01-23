package com.mballem.curso.security.validation.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.mballem.curso.security.validation.AgendamentoDate;

public class AgendamentoDateValidator implements ConstraintValidator<AgendamentoDate, String> {

	@Override
	public void initialize(AgendamentoDate constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String valueDate, ConstraintValidatorContext context) {
		LocalDate currentDate = LocalDate.now();
		LocalDate date;

		try {
			date = LocalDate.parse(valueDate, DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (Exception e) {
			return false;
		}

		return (date.isAfter(currentDate));
	}

}
