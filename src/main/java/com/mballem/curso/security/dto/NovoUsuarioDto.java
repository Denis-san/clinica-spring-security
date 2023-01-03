package com.mballem.curso.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.validation.Password;

public class NovoUsuarioDto {

	@Email
	@NotEmpty
	private String email;

	@Password
	private String senha;

	public NovoUsuarioDto() {
		super();
	}

	public NovoUsuarioDto(String email) {
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

	public Usuario toNewUsuario() {
		Usuario usuario = new Usuario();

		BeanUtils.copyProperties(this, usuario);
		
		usuario.setAtivo(false);
		usuario.addPerfil(PerfilTipo.PACIENTE);

		return usuario;
	}

}
