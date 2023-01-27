package com.mballem.curso.security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.mballem.curso.security.validation.Password;

public class RecoveryAccountDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Email
	@NotEmpty
	private String email;

	@Password
	private String senha;

	@NotEmpty
	@Size(max = 6, min = 6)
	private String codigoVerificador;

	public RecoveryAccountDTO() {
		
	}

	public RecoveryAccountDTO(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}

}
