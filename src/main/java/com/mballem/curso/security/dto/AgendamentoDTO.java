package com.mballem.curso.security.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.validation.AgendamentoDate;

public class AgendamentoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull(message = "A especialidade não pode estar vazia")
	@Valid
	private EspecialidadeDTO especialidade;

	@NotNull(message = "O médico deve ser selecionado")
	private Long medicoId;

	@NotNull(message = "O horário deve ser especificado")
	@Valid
	private HorarioDTO horario;

	@AgendamentoDate(message = "Data inválida")
	private String dataConsulta;

	public AgendamentoDTO() {

	}

	public AgendamentoDTO(Agendamento agendamento) {
		this.id = agendamento.getId();
		this.dataConsulta = agendamento.getDataConsulta().format(DateTimeFormatter.ISO_LOCAL_DATE);
		this.medicoId = agendamento.getMedico().getId();
		this.especialidade = toNewEspecialidadeDTO(agendamento.getEspecialidade());
		this.horario = toNewHorarioDTO(agendamento.getHorario());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EspecialidadeDTO getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeDTO especialidade) {
		this.especialidade = especialidade;
	}

	public Long getMedicoId() {
		return medicoId;
	}

	public void setMedicoId(Long medicoId) {
		this.medicoId = medicoId;
	}

	public String getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public HorarioDTO getHorario() {
		return horario;
	}

	public void setHorario(HorarioDTO horario) {
		this.horario = horario;
	}

	public Agendamento toNewAgendamento(Paciente paciente, Especialidade especialidade) {
		Agendamento agendamento = new Agendamento();

		agendamento.setDataConsulta(LocalDate.parse(dataConsulta, DateTimeFormatter.ISO_LOCAL_DATE));
		agendamento.setEspecialidade(especialidade);
		agendamento.setHorario(horario.toNewHorario());
		agendamento.setMedico(new Medico(medicoId));
		agendamento.setPaciente(paciente);

		return agendamento;
	}
	
	public Agendamento toAgendamento(Paciente paciente, Especialidade especialidade) {
		Agendamento agendamento = new Agendamento();
		
		agendamento.setId(id);
		agendamento.setDataConsulta(LocalDate.parse(dataConsulta, DateTimeFormatter.ISO_LOCAL_DATE));
		agendamento.setEspecialidade(especialidade);
		agendamento.setHorario(horario.toNewHorario());
		agendamento.setMedico(new Medico(medicoId));
		agendamento.setPaciente(paciente);

		return agendamento;
	}

	private EspecialidadeDTO toNewEspecialidadeDTO(Especialidade especialidade) {
		EspecialidadeDTO especialidadeDTO = new EspecialidadeDTO();
		
		BeanUtils.copyProperties(especialidade, especialidadeDTO);
		
		return especialidadeDTO;
	}
	
	private HorarioDTO toNewHorarioDTO(Horario horario) {
		HorarioDTO horarioDto = new HorarioDTO();
		
		BeanUtils.copyProperties(horario, horarioDto);
		
		return horarioDto;
	}

	
}
