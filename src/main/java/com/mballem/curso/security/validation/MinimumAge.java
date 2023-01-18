package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import com.mballem.curso.security.validation.validator.MinimumAgeValidator;

@ConstraintComposition(CompositionType.AND)
@NotNull
@Constraint(validatedBy = {MinimumAgeValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface MinimumAge {

	String message() default "A senha não é valida";
	
	int min() default 0;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
