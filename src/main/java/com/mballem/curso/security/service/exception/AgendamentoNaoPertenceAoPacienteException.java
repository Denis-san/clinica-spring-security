package com.mballem.curso.security.service.exception;

public class AgendamentoNaoPertenceAoPacienteException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AgendamentoNaoPertenceAoPacienteException(String msg) {
		super(msg);
	}
}
