package com.mballem.curso.security.validation.validator;

import java.util.Arrays;
import java.util.Collection;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.validation.ValidPerfil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PerfilValidator implements ConstraintValidator<ValidPerfil, Collection<Perfil>> {

	private static final Long ADMIN = PerfilTipo.ADMIN.getCod();
	private static final Long MEDICO = PerfilTipo.MEDICO.getCod();
	private static final Long PACIENTE = PerfilTipo.PACIENTE.getCod();

	@Override
	public void initialize(ValidPerfil constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Collection<Perfil> perfis, ConstraintValidatorContext context) {

		if (perfis.size() > 2) {
			return false;
		}

		if (perfis.containsAll(Arrays.asList(new Perfil(ADMIN), new Perfil(PACIENTE))) ||
				perfis.containsAll(Arrays.asList(new Perfil(MEDICO), new Perfil(PACIENTE)))) {
			return false;
		}
		
		return true;
	}

}
