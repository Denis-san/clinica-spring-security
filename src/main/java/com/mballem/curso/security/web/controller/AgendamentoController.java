package com.mballem.curso.security.web.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.AgendamentoService;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService service;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private EspecialidadeService especialidadeService;

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping({ "/agendar" })
	public String agendarConsulta(Agendamento agendamento) {
		return "agendamento/cadastro";
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
			@PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
		return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
	}

	@PreAuthorize("hasAuthority('PACIENTE')")
	@PostMapping({ "/salvar" })
	public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {

		Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		
		if(paciente.hasNotId()) {
			attr.addFlashAttribute("aviso", "Você precisa preencher seus dados cadastrais antes de prosseguir");
			return "redirect:/pacientes/dados";
		}

		String titulo = agendamento.getEspecialidade().getTitulo();

		Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();

		agendamento.setEspecialidade(especialidade);
		agendamento.setPaciente(paciente);
		service.salvar(agendamento);

		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso!");

		return "redirect:/agendamentos/agendar";
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping({ "/historico/paciente", "/historico/consultas" })
	public String hitorico() {
		return "agendamento/historico-paciente";
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoAgendamentosPorPaciente(HttpServletRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {
			return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
		}

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {
			return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
		}

		return ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/editar/consulta/{id}")
	public String preEditarConsulta(@PathVariable("id") Long id, ModelMap model, @AuthenticationPrincipal User user) {

		Agendamento agendamento = service.buscarAgendamentoPorIdEUsuario(id, user.getUsername());
		model.addAttribute("agendamento", agendamento);

		return "agendamento/cadastro";
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@PostMapping("/editar")
	public String editarConsulta(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidade(especialidade);

		service.editar(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso!");

		return "redirect:/agendamentos/agendar";

	}

	@PreAuthorize("hasAnyAuthority('PACIENTE')")
	@GetMapping("/excluir/consulta/{id}")
	public String excluirConsulta(@PathVariable("id") Long id, RedirectAttributes attr,  @AuthenticationPrincipal User user) {
		service.removerConsultaPorIdEUsuarioEmail(id, user.getUsername());

		attr.addFlashAttribute("sucesso", "Consulta excluida com sucesso!");

		return "redirect:/agendamentos/historico/paciente";
	}

}
