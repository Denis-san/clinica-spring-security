package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository repository;

	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(Long id) {
		return repository.findByUsuarioId(id).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void salvar(Medico medico) {
		repository.save(medico);
	}

	@Transactional(readOnly = false)
	public void editar(Medico medico) {
		Medico otherMedico = repository.findById(medico.getId()).get();

		otherMedico.setCrm(medico.getCrm());
		otherMedico.setDtInscricao(medico.getDtInscricao());
		otherMedico.setNome(medico.getNome());

		if (!medico.getEspecialidades().isEmpty()) {
			otherMedico.getEspecialidades().addAll(medico.getEspecialidades());
		}

		repository.save(otherMedico);

	}

	@Transactional(readOnly = true)
	public Medico findByEmail(String email) {
		return repository.findByUsuarioEmail(email).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
		Medico medico = repository.findById(idMed).get();
		medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));

	}

}
