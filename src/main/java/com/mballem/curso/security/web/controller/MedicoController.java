package com.mballem.curso.security.web.controller;

import jakarta.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.dto.MedicoDTO;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;
import com.mballem.curso.security.service.exception.EspecialidadeNaoPerteceAoMedicoException;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoService service;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping({ "/dados" })
	public String abrirPorMedico(@ModelAttribute("medico") MedicoDTO medicoDto, ModelMap model,
			@AuthenticationPrincipal User user) {

		if (medicoDto.getId() == null) {
			medicoDto = new MedicoDTO(service.findByEmail(user.getUsername()));
			model.addAttribute("medico", medicoDto);
		}

		return "medico/cadastro";
	}

	@PostMapping({ "/salvar" })
	public String salvar(@ModelAttribute("medico") @Valid MedicoDTO medicoDto, BindingResult bdResult,
			RedirectAttributes attr, @AuthenticationPrincipal User user) {

		if (bdResult.hasErrors()) {
			attr.addFlashAttribute("errorsNome", bdResult.getFieldErrors("nome"));
			attr.addFlashAttribute("errorsCrm", bdResult.getFieldErrors("crm"));
			attr.addFlashAttribute("errorsDtInscricao", bdResult.getFieldErrors("dtInscricao"));
			return "redirect:/medicos/dados";
		}

		if ((medicoDto.getUsuario() == null) || (medicoDto.getId() == null && medicoDto.getUsuario().hasNotId())) {
			Usuario usuario = usuarioService.buscarUsuarioPorEmail(user.getUsername());
			medicoDto.setUsuario(usuario);
		}

		try {
			service.salvar(medicoDto.toNewMedico());
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("errorConstraint",
					((ConstraintViolationException) e.getCause()).getConstraintName());
			return "redirect:/medicos/dados";
		}

		attr.addFlashAttribute("sucesso", "Sucesso!");
		attr.addFlashAttribute("medico", medicoDto);

		return "redirect:/medicos/dados";
	}

	@PostMapping({ "/editar" })
	public String editar(@ModelAttribute("medico") @Valid MedicoDTO medicoDto, BindingResult bdResult,
			RedirectAttributes attr, @AuthenticationPrincipal User user) {

		if (bdResult.hasErrors()) {
			attr.addFlashAttribute("errorsNome", bdResult.getFieldErrors("nome"));
			attr.addFlashAttribute("errorsCrm", bdResult.getFieldErrors("crm"));
			attr.addFlashAttribute("errorsDtInscricao", bdResult.getFieldErrors("dtInscricao"));
			return "redirect:/medicos/dados";
		}

		try {
			service.editar(medicoDto.toMedico(), user.getUsername());
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("errorConstraint",
					((ConstraintViolationException) e.getCause()).getConstraintName());
			return "redirect:/medicos/dados";
		}

		attr.addFlashAttribute("sucesso", "Dados editados com sucesso!");
		attr.addFlashAttribute("medico", medicoDto);

		return "redirect:/medicos/dados";
	}

	@GetMapping({ "/id/{idMed}/excluir/especializacao/{idEsp}" })
	public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed, @PathVariable("idEsp") Long idEsp,
			RedirectAttributes attr, @AuthenticationPrincipal User user) {

		Medico medico = service.findByEmail(user.getUsername());

		if ((medico.getId() != idMed) || (medico.getEspecialidades().contains(new Especialidade(idEsp)) == false)) {
			throw new EspecialidadeNaoPerteceAoMedicoException("Essa especialidade não pertence a este médico!");
		}

		if (service.existeEspecialidadeAgendada(idMed, idEsp)) {
			attr.addFlashAttribute("falha", "Existem consultas agendadas para essa especialidade");
		} else {
			service.excluirEspecialidadePorMedico(idMed, idEsp);
			attr.addFlashAttribute("sucesso", "Especialidade removida!");
		}
		return "redirect:/medicos/dados";
	}

	@GetMapping("/especialidade/titulo/{titulo}")
	public ResponseEntity<?> getMedicosPorEspecialidade(@PathVariable("titulo") String titulo) {
		return ResponseEntity.ok(service.buscarMedicosPorEspecialidade(titulo));
	}

}
