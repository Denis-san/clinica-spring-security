package com.mballem.curso.security.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.validation.MinimumAge;

public class MedicoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull(message = "O nome não deve estar vazio")
	@Size(min = 5, message = "O nome deve ter no mínimo 5 caracteres")
	private String nome;

	@NotNull(message = "O CRM não deve estar vazio")
	private Integer crm;

	@NotNull(message = "A data de inscrição não deve estar vazia")
	@MinimumAge(min = 1, message = "Data inválida")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dtInscricao;

	@NotNull(message = "A especialidade não pode estar vazia")
	@NotEmpty(message = "O médico deve possuir no mínimo uma especialidade")
	private Set<Especialidade> especialidades;

	private List<Agendamento> agendamentos;

	private Usuario usuario;

	public MedicoDTO() {

	}

	public MedicoDTO(Medico medico) {
		this.id = medico.getId();
		this.nome = medico.getNome();
		this.crm = medico.getCrm();
		this.dtInscricao = medico.getDtInscricao();
		this.especialidades = medico.getEspecialidades();
		this.agendamentos = medico.getAgendamentos();
		this.usuario = medico.getUsuario();
	}

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

	public Integer getCrm() {
		return crm;
	}

	public void setCrm(Integer crm) {
		this.crm = crm;
	}

	public LocalDate getDtInscricao() {
		return dtInscricao;
	}

	public void setDtInscricao(LocalDate dtInscricao) {
		this.dtInscricao = dtInscricao;
	}

	public Set<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Medico toMedico() {
		Medico medico = new Medico();
		BeanUtils.copyProperties(this, medico);
		return medico;
	}

	public Medico toNewMedico() {
		Medico medico = new Medico();
		BeanUtils.copyProperties(this, medico);
		medico.setId(null);
		return medico;
	}

}
