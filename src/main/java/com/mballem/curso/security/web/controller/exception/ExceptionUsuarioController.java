package com.mballem.curso.security.web.controller.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

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

}
