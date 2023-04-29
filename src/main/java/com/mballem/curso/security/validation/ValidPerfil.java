package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mballem.curso.security.validation.validator.PerfilValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PerfilValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidPerfil {

	String message() default "Profile mismatch";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
