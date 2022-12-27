package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository repository;

	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioEmail(String email) {
		return repository.findByUsuarioEmail(email).orElse(new Paciente());
	}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente) {
		repository.save(paciente);

	}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) {
		Paciente otherPaciente = repository.findById(paciente.getId()).get();

		otherPaciente.setNome(paciente.getNome());
		otherPaciente.setDtNascimento(paciente.getDtNascimento());

		repository.save(otherPaciente);

	}

}
