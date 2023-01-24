package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.mballem.curso.security.validation.validator.MinimumAgeValidator;

@ReportAsSingleViolation
@Constraint(validatedBy = {MinimumAgeValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface MinimumAge {

	String message() default "Invalid date";
	
	int min() default 0;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
