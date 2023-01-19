package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.mballem.curso.security.validation.validator.FieldsMatchValidator;

@Constraint(validatedBy = FieldsMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsMatch {

	String message() default "Field values do not match!";

	String field();

	String fieldVerify();

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface Fields {
		FieldsMatch[] value();
	}

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
