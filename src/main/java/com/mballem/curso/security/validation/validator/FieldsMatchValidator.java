package com.mballem.curso.security.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import com.mballem.curso.security.validation.FieldsMatch;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {

	private String field;
	private String fieldVerify;

	@Override
	public void initialize(FieldsMatch constraintAnnotation) {
		this.field = constraintAnnotation.field();
		this.fieldVerify = constraintAnnotation.fieldVerify();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
		Object fieldVerifyValue = new BeanWrapperImpl(value).getPropertyValue(fieldVerify);

		if (fieldValue == null) {
			return false;
		}

		return fieldValue.equals(fieldVerifyValue);

	}
}
