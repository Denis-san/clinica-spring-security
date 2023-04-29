package com.mballem.curso.security.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.validation.Password;
import com.mballem.curso.security.validation.ValidPerfil;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class NovoUsuarioDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	@Email
	@NotEmpty(message = "O email não deve estar vazio!")
	private String email;

	@Password
	private String senha;
	
	@NotEmpty
	@ValidPerfil(message = "Paciente não pode ser admin e/ou Médico")
	private List<Perfil> perfis;
	
	private boolean ativo;
	
	public NovoUsuarioDTO() {
		super();
	}

	public NovoUsuarioDTO(String email) {
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

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		return "NovoUsuarioDto [email=" + email + ", senha=" + senha + "]";
	}

	public Usuario toNewUsuario() {
		Usuario usuario = new Usuario();
		BeanUtils.copyProperties(this, usuario);
		return usuario;
	}

}
