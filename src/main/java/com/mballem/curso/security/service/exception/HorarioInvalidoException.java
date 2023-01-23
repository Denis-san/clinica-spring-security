package com.mballem.curso.security.service.exception;

public class HorarioInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HorarioInvalidoException(String msg) {
		super(msg);
	}
}
