package com.mballem.curso.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.dto.NovaEspecialidadeDto;
import com.mballem.curso.security.service.EspecialidadeService;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

	@Autowired
	private EspecialidadeService service;

	@GetMapping({ "", "/" })
	public String abrirPaginaEspecialidade(@ModelAttribute("especialidade") NovaEspecialidadeDto especialidadeDto) {
		return "especialidade/especialidade";
	}

	@PostMapping("/salvar")
	public String salvarEspecialidade(@Valid @ModelAttribute("especialidade") NovaEspecialidadeDto especialidadeDto,
			BindingResult bdResult, RedirectAttributes attr) {

		if (bdResult.hasErrors()) {
			return "especialidade/especialidade";
		}

		try {
			service.salvar(especialidadeDto.toNewEspecialidade());
			attr.addFlashAttribute("sucesso", "Sucesso!");
		} catch (DataIntegrityViolationException err) {
			attr.addFlashAttribute("falha", "Já existe essa especialidade na base de dados!");
		}

		return "redirect:/especialidades/";

	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarEspecialidades(request));
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("especialidade", service.buscarPorId(id));

		Boolean editMode = true;
		model.addAttribute("editMode", editMode);
		return "especialidade/especialidade";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.removerPorId(id);
		attr.addFlashAttribute("sucesso", "Sucesso!");
		return "redirect:/especialidades";
	}

	@GetMapping("/titulo")
	public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo) {
		List<String> especialidades = service.buscarEspecialidadeByTermo(termo);
		return ResponseEntity.ok(especialidades);
	}

	@GetMapping("/datatables/server/medico/{id}")
	public ResponseEntity<?> getEspecialidadesPorMedico(@PathVariable("id") Long id, HttpServletRequest request) {

		return ResponseEntity.ok(service.buscarEspecialidadePorMedico(id, request));
	}

}
