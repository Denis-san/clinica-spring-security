package com.mballem.curso.security.service.exception;

public class EspecialidadeNaoPerteceAoMedicoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EspecialidadeNaoPerteceAoMedicoException(String msg) {
		super(msg);
	}

}
