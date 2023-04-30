package com.mballem.curso.security.service.exception;

public class PerfilNaoCondizComCadastroException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PerfilNaoCondizComCadastroException(String msg) {
		super(msg);
	}
}
