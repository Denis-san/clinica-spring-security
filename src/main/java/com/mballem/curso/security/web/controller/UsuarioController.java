package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
