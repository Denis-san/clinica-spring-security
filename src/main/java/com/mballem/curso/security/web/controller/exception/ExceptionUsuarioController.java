package com.mballem.curso.security.web.controller.exception;

import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.service.exception.AgendamentoNaoPertenceAoPacienteException;

@ControllerAdvice
public class ExceptionUsuarioController {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 404);
		modelAndView.addObject("error", "Algo não deu certo!");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(AcessoNegadoException.class)
	public ModelAndView acessoNegadoExpception(AcessoNegadoException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 403);
		modelAndView.addObject("error", "Algo não deu certo!");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(AgendamentoNaoPertenceAoPacienteException.class)
	public ModelAndView agendamentoNaoPertenceAoPacienteException(AgendamentoNaoPertenceAoPacienteException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 400);
		modelAndView.addObject("error", "Algo não deu certo!");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ModelAndView noSuchElementException(NoSuchElementException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 400);
		modelAndView.addObject("error", "Hmm não conseguimos processar a solicitação...");
		modelAndView.addObject("message", "Recurso indisponível ou não encontrado!");
		return modelAndView;
	}
	
}
