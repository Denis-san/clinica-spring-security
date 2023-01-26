package com.mballem.curso.security.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.validation.MinimumAge;

public class PacienteDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull(message = "O nome não pode estar nulo")
	@NotEmpty(message = "O nome não pode estar vazio")
	@Size(min = 5, max = 50, message = "O nome deve ter entre 5 e 50 caracteres")
	private String nome;

	@DateTimeFormat(iso = ISO.DATE)
	@MinimumAge(min = 10, message = "Data inválida! O paciente deve ter no mínimo 10 anos de idade")
	private LocalDate dtNascimento;

	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Paciente toPaciente() {
		Paciente paciente = new Paciente();

		BeanUtils.copyProperties(this, paciente);

		return paciente;
	}

}
