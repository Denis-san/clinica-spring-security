package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.dto.ChangePasswordDTO;
import com.mballem.curso.security.dto.NovoUsuarioDTO;
import com.mballem.curso.security.dto.NovoUsuarioPacienteDTO;
import com.mballem.curso.security.dto.RecoveryAccountDTO;
import com.mballem.curso.security.service.EmailService;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private MedicoService medicoService;

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
	public String salvarUsuarioPorAdministrador(@Valid NovoUsuarioDTO usuarioDto, BindingResult bdResult,
			RedirectAttributes attr) {

		if (bdResult.hasErrors()) {
			attr.addFlashAttribute("errorsPerfil", bdResult.getFieldErrors("perfis"));
			attr.addFlashAttribute("errorsSenha", bdResult.getFieldErrors("senha"));
			attr.addFlashAttribute("errosEmail", bdResult.getFieldErrors("email"));

			return "redirect:/u/novo/cadastro/usuario";
		}

		try {
			service.salvarUsuario(usuarioDto.toNewUsuario());
			attr.addFlashAttribute("sucesso", "Sucesso!");
		} catch (DataIntegrityViolationException err) {
			attr.addFlashAttribute("falha",
					"Cadastro não realizado! Já existe esse email cadastrado em nossa aplicação!");
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

			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);

			if (medico.hasNotId()) {
				return new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)));
			} else {
				return new ModelAndView("medico/cadastro", "medico", medico);
			}

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

	@GetMapping("/editar/senha")
	public String abrirEditarSenha(@ModelAttribute("chPassObjt") ChangePasswordDTO chPassObjt) {
		return "usuario/editar-senha";
	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@Valid @ModelAttribute("chPassObjt") ChangePasswordDTO chPassObjt, BindingResult bdResult,
			@AuthenticationPrincipal User user, RedirectAttributes attr) {

		if (bdResult.hasErrors()) {
			attr.addFlashAttribute("errorsPass1", bdResult.getFieldErrors("password1"));
			attr.addFlashAttribute("errorsPass2", bdResult.getFieldErrors("password2"));
			attr.addFlashAttribute("errorsPassCurrentPass", bdResult.getFieldErrors("currentPassword"));
			attr.addFlashAttribute("fieldsMatchError", bdResult.getAllErrors().stream()
					.filter(e -> e.getCode().equals("FieldsMatch")).findFirst().orElse(null));

			return "redirect:/u/editar/senha";
		}

		Usuario usuario = service.buscarUsuarioPorEmail(user.getUsername());

		if (!service.isSenhaCorreta(chPassObjt.getCurrentPassword(), usuario.getSenha())) {
			attr.addFlashAttribute("falha", "Senha ATUAL incorreta!");
			return "redirect:/u/editar/senha";
		}

		service.alterarSenha(usuario, chPassObjt.getPassword2());
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso!");

		return "redirect:/u/editar/senha";

	}

	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {
		return "cadastrar-se";
	}

	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado(Usuario usuario) {

		return "fragments/mensagem";
	}

	@PostMapping("/cadastro/paciente/salvar")
	public String salvarCadastroPaciente(@Valid @ModelAttribute("usuario") NovoUsuarioPacienteDTO usuarioDto,
			BindingResult bdResult) throws MessagingException {

		if (bdResult.hasErrors()) {
			return "cadastrar-se";
		}

		try {
			service.salvarCadastroPaciente(usuarioDto);
		} catch (DataIntegrityViolationException ex) {
			bdResult.rejectValue("email", "email", "Oops! Já esse email ja existe em nosso sistema!");
			return "cadastrar-se";
		} catch (MailSendException e) {
			bdResult.rejectValue("email", "email", "Não encontramos esse endereço de email... Verfique se não errou");
			return "cadastrar-se";
		}

		return "redirect:/u/cadastro/realizado";

	}

	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {

		service.ativarCadastroPaciente(codigo);

		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo", "Cadastro Ativado com sucesso!");
		attr.addFlashAttribute("texto", "Pronto! Seu cadastro está ativo.");
		attr.addFlashAttribute("subtexto", "Siga com seu login/senha");

		return "redirect:/login";
	}

	@GetMapping("/p/redefinir/senha")
	public String pedidoRedefinirSenha() {
		return "usuario/pedido-recuperar-senha";
	}

	@PostMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model, RedirectAttributes attr) throws MessagingException {

		if (email == null || email.isBlank() || EmailService.emailMatchesRegex(email) == false) {
			attr.addFlashAttribute("falha", "Email inválido!");
			return "redirect:/u/p/redefinir/senha";
		}

		service.pedidoRedefinicaoDeSenha(email);

		model.addAttribute("sucesso",
				"Em instantes você receberá um e-mail para prosseguir com a redefinição da sua senha.");
		model.addAttribute("recoveryAccount", new RecoveryAccountDTO(email));

		return "usuario/recuperar-senha";

	}

	@PostMapping("/p/nova/senha")
	public String confirmacaoDeRedefinicaoDeSenha(
			@ModelAttribute("recoveryAccount") @Valid RecoveryAccountDTO recoveryAcc, BindingResult bdResult,
			ModelMap model) {

		if (bdResult.hasErrors()) {
			return "usuario/recuperar-senha";
		}

		Usuario usuario = service.buscarUsuarioPorEmail(recoveryAcc.getEmail());

		if (!usuario.getCodigoVerificador().equals(recoveryAcc.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere!");
			return "usuario/recuperar-senha";
		}

		usuario.setCodigoVerificador(null);

		service.alterarSenha(usuario, recoveryAcc.getSenha());

		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha redefinida com sucesso!");
		model.addAttribute("texto", "Você ja pode logar no sistema.");

		return "login";
	}

}
