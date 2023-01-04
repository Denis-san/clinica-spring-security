package com.mballem.curso.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.mballem.curso.security.domain.Especialidade;

public class NovaEspecialidadeDto {

	@NotEmpty(message = "O titulo da especialidade não pode estar vazio")
	private String titulo;

	@Size(max = 500, message = "A descrição não deve ultrapassar 500 caracteres")
	private String descricao;

	public NovaEspecialidadeDto() {

	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Especialidade toNewEspecialidade() {
		Especialidade especialidade = new Especialidade();
		
		BeanUtils.copyProperties(this, especialidade);
		
		return especialidade;
	}

}
