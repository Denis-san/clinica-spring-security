package com.mballem.curso.security.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.dto.PacienteDTO;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private PacienteService service;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/dados")
	public String abrirCadastroEditar(@ModelAttribute("paciente") PacienteDTO pacienteDto,
			@AuthenticationPrincipal User user, ModelMap model) {

		Paciente paciente = service.buscarPorUsuarioEmail(user.getUsername());

		if (paciente.hasId()) {
			model.addAttribute("paciente", paciente);
			return "paciente/editar";
		}

		pacienteDto.setUsuario(new Usuario(user.getUsername()));
		return "paciente/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute("paciente") @Valid PacienteDTO pacienteDto, BindingResult bdResult,
			@AuthenticationPrincipal User user, ModelMap model) {

		if (bdResult.hasErrors()) {
			return "paciente/cadastro";
		}

		Usuario usuario = usuarioService.buscarUsuarioPorEmail(user.getUsername());

		if (!usuarioService.isSenhaCorreta(pacienteDto.getUsuario().getSenha(), usuario.getSenha())) {
			model.addAttribute("falha", "Senha incorreta!");
			return "paciente/cadastro";
		}

		pacienteDto.setUsuario(usuario);
		service.salvar(pacienteDto.toPaciente());
		model.addAttribute("sucesso", "Dados inseridos com sucesso!");

		return "paciente/cadastro";
	}

	@PostMapping("/editar")
	public String editar(@ModelAttribute("paciente") @Valid PacienteDTO pacienteDto, BindingResult bdResult,
			@AuthenticationPrincipal User user, ModelMap model) {

		if (bdResult.hasErrors()) {
			return "paciente/editar";
		}
		
		Paciente pacienteDb = service.buscarPorUsuarioEmail(user.getUsername());

		if (!pacienteDto.getId().equals(pacienteDb.getId())) {
			throw new IllegalArgumentException("Algo deu errado!");
		}

		Usuario usuario = pacienteDb.getUsuario();

		if (!usuarioService.isSenhaCorreta(pacienteDto.getUsuario().getSenha(), usuario.getSenha())) {
			model.addAttribute("falha", "Senha incorreta!");
			return "paciente/editar";
		}

		service.editar(pacienteDto.toPaciente());
		model.addAttribute("sucesso", "Dados atualizados com sucesso!");
		return "paciente/editar";
	}

}
