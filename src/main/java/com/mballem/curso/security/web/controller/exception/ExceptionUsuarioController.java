package com.mballem.curso.security.web.controller.exception;

import java.util.NoSuchElementException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.service.exception.AgendamentoNaoPertenceAoPacienteException;
import com.mballem.curso.security.service.exception.EspecialidadeNaoPerteceAoMedicoException;
import com.mballem.curso.security.service.exception.HorarioInvalidoException;
import com.mballem.curso.security.service.exception.PerfilNaoCondizComCadastroException;

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

	@ExceptionHandler({ AgendamentoNaoPertenceAoPacienteException.class,
			EspecialidadeNaoPerteceAoMedicoException.class })
	public ModelAndView agendamentoNaoPertenceAoPacienteException(Exception exception) {
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

	@ExceptionHandler({ ConversionFailedException.class, IllegalArgumentException.class, DataAccessException.class })
	public ModelAndView methodArgumentTypeMismatchException() {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 400);
		modelAndView.addObject("error", "Hmm não conseguimos processar a solicitação...");
		modelAndView.addObject("message", "Verifique se inseriu algo incorreto!");
		return modelAndView;
	}

	@ExceptionHandler(HorarioInvalidoException.class)
	public ModelAndView horarioInvalidoException(HorarioInvalidoException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 400);
		modelAndView.addObject("error", "Hmm... Não conseguimos processar a solicitação.");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(PerfilNaoCondizComCadastroException.class)
	public ModelAndView perfilNaoCondizComCadastroException(PerfilNaoCondizComCadastroException exception) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("status", 400);
		modelAndView.addObject("error", "Hmm... Não conseguimos processar a solicitação.");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}
}
