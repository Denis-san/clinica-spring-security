package com.mballem.curso.security.service;

import java.util.List;

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
	public void editar(Medico medico, String email) {
		Medico otherMedico = findByEmail(email);

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

	@Transactional(readOnly = true)
	public List<Medico> buscarMedicosPorEspecialidade(String titulo) {
		return repository.findMedicosByEspecialidade(titulo);
	}

	@Transactional(readOnly = true)
	public boolean existeEspecialidadeAgendada(Long idMed, Long idEsp) {
		return repository.hasEspecialidadesAgendadas(idMed, idEsp).isPresent();
	}

}
