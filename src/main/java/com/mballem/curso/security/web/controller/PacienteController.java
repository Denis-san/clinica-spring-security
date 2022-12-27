package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
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
	public String abrirCadastro(Paciente paciente, @AuthenticationPrincipal User user, ModelMap model) {

		paciente = service.buscarPorUsuarioEmail(user.getUsername());

		if (paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}

		model.addAttribute("paciente", paciente);

		return "paciente/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, @AuthenticationPrincipal User user, ModelMap model) {

		Usuario usuario = usuarioService.buscarUsuarioPorEmail(user.getUsername());
		
		if(usuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
			paciente.setUsuario(usuario);
			service.salvar(paciente);
			model.addAttribute("sucesso", "Dados atualizados com sucesso!");
		}else {
			model.addAttribute("falha", "Senha incorreta!");
		}

		return "paciente/cadastro";
	}

}
