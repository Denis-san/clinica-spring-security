package com.mballem.curso.security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.AgendamentoRepository;
import com.mballem.curso.security.repository.projection.HistoricoPaciente;
import com.mballem.curso.security.service.exception.AgendamentoNaoPertenceAoPacienteException;
import com.mballem.curso.security.service.exception.HorarioInvalidoException;
import com.mballem.curso.security.web.controller.exception.AcessoNegadoException;

@Service
public class AgendamentoService {

	@Autowired
	private AgendamentoRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = true)
	public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
		return repository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
	}

	@Transactional(readOnly = false)
	public void salvar(Agendamento agendamento) {

		Long medicoId = agendamento.getMedico().getId();
		LocalDate dataConsulta = agendamento.getDataConsulta();

		List<Horario> horariosDisponiveis = repository.findByMedicoIdAndDataNotHorarioAgendado(medicoId,
				dataConsulta);

		if (horariosDisponiveis.contains(agendamento.getHorario()) == false) {
			throw new HorarioInvalidoException("Horario inválido");	
		}

		repository.save(agendamento);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = repository.findHistoricoByPacienteEmail(email, datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Object buscarHistoricoPorMedicoEmail(String email, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = repository.findHistoricoByMedicoEmail(email, datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Agendamento buscarAgendamentoPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void editar(Agendamento agendamento, String email) {
		Agendamento otherAgendamento = buscarAgendamentoPorIdEUsuario(agendamento.getId(), email);
		otherAgendamento.setDataConsulta(agendamento.getDataConsulta());
		otherAgendamento.setEspecialidade(agendamento.getEspecialidade());
		otherAgendamento.setHorario(agendamento.getHorario());
		otherAgendamento.setMedico(agendamento.getMedico());

		repository.save(otherAgendamento);

	}

	@Transactional(readOnly = true)
	public Agendamento buscarAgendamentoPorIdEUsuario(Long id, String email) {
		return repository.findByIdAndPacienteOrMedicoEmail(id, email)
				.orElseThrow(() -> new AcessoNegadoException("Acesso negado!"));
	}

	@Transactional(readOnly = false)
	public void removerConsultaPorIdEUsuarioEmail(Long id, String emailPaciente) {

		repository.findByIdAndPacienteOrMedicoEmail(id, emailPaciente).orElseThrow(
				() -> new AgendamentoNaoPertenceAoPacienteException("O agendamento não pertence a este paciente"));

		repository.deleteById(id);

	}

}
