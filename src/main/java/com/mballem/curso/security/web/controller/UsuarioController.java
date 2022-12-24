package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("/u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping("/novo/cadastro/usuario")
	public String cadastroUsuario(Usuario usuario) {

		return "usuario/cadastro";
	}

	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}

	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}

	@PostMapping("/cadastro/salvar")
	public String salvarUsuarioPorAdministrador(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();

		if (perfis.size() > 2 || (perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L)))
				|| perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L))))) {

			attr.addFlashAttribute("falha", "Paciente não pode ser admin e/ou Médico");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				service.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Sucesso!");
			} catch (DataIntegrityViolationException err) {
				attr.addFlashAttribute("falha",
						"Cadastro não realizado! Pois esse email ja existe em nossa aplicação!");
			}
		}

		return "redirect:/u/novo/cadastro/usuario";

	}

	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
		return new ModelAndView("usuario/cadastro", "usuario", service.buscarUsuarioPorId(id));
	}

	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarDadosPessoais(@PathVariable("id") Long usuarioId,
			@PathVariable("perfis") Long[] perfisId) {

		Usuario usuario = service.buscarUsuarioPorIdEPerfis(usuarioId, perfisId);

		if (usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod()))
				&& !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			return new ModelAndView("usuario/cadastro", "usuario", usuario);
		}

		if (usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			return new ModelAndView("especialidade/especialidade");
		}

		if (usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView modelAndView = new ModelAndView("error");
			modelAndView.addObject("status", 403);
			modelAndView.addObject("error", "Área restrita!");
			modelAndView.addObject("message", "Os dados de pacientes são restritos a eles");
			return modelAndView;
		}

		return new ModelAndView("redirect:/u/lista");
	}

}
