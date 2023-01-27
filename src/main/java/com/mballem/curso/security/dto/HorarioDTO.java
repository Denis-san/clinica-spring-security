package com.mballem.curso.security.dto;

import java.io.Serializable;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.mballem.curso.security.domain.Horario;

public class HorarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long id;

	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime horaMinuto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getHoraMinuto() {
		return horaMinuto;
	}

	public void setHoraMinuto(LocalTime horaMinuto) {
		this.horaMinuto = horaMinuto;
	}

	public Horario toNewHorario() {
		Horario horario = new Horario();

		horario.setId(id);
		horario.setHoraMinuto(horaMinuto);

		return horario;
	}
}
