package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import com.mballem.curso.security.validation.validator.AgendamentoDateValidator;

@ConstraintComposition(CompositionType.AND)
@NotNull
@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
@ReportAsSingleViolation
@Constraint(validatedBy = {AgendamentoDateValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface AgendamentoDate {

	String message() default "The date is not valid";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
