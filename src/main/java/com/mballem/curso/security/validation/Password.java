package com.mballem.curso.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

@ConstraintComposition(CompositionType.AND)
@NotEmpty(message = "A senha não pode estar vazia")
@Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
@Pattern.List({ 
	@Pattern(regexp = "^(?=.*[0-9]).*$", message = "A senha deve ter no mínimo um numero"),
	@Pattern(regexp = "^(?=.*[a-z]).*$", message = "A senha deve ter no mínimo uma letra minúscula"),
	@Pattern(regexp = "^(?=.*[A-Z]).*$", message = "A senha deve ter no mínimo uma letra maiúscula"),
	@Pattern(regexp = "^(?=.*[@#$%^&+=]).*$", message = "A senha deve ter no mínimo um caractere especial")
})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })

public @interface Password {

	String message() default "A senha não é valida";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
