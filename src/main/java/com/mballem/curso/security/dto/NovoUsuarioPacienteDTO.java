package com.mballem.curso.security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.validation.Password;

public class NovoUsuarioPacienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	@Email
	@NotEmpty(message = "O email não deve estar vazio!")
	private String email;

	@Password
	private String senha;

	public NovoUsuarioPacienteDTO() {
		super();
	}

	public NovoUsuarioPacienteDTO(String email) {
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

	@Override
	public String toString() {
		return "NovoUsuarioDto [email=" + email + ", senha=" + senha + "]";
	}

	public Usuario toNewUsuarioPaciente() {
		Usuario usuario = new Usuario();

		BeanUtils.copyProperties(this, usuario);
		
		usuario.setAtivo(false);
		usuario.addPerfil(PerfilTipo.PACIENTE);

		return usuario;
	}

}
