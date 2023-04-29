package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioRepository;
import com.mballem.curso.security.web.controller.exception.AcessoNegadoException;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private Datatables datatables;

	@Autowired
	private EmailService emailService;

	@Transactional(readOnly = true)
	public Usuario buscarUsuarioPorEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = repository.findByEmailAndAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado!"));

		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis())));
	}

	private String[] getAuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];

		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}

		return authorities;

	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		} else {
			page = repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		}

		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		repository.save(usuario);

	}

	@Transactional(readOnly = true)
	public Usuario buscarUsuarioPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarUsuarioPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		return repository.findByIdAndPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
	}

	public boolean isSenhaCorreta(String senhaInserida, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaInserida, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String novaSenha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
		repository.save(usuario);
	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException, MailSendException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());

		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);

		repository.save(usuario);

		emailDeConfirmacaoDeCadastro(usuario.getEmail());

	}

	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailAtivo(String email) {
		return repository.findByEmailAndAtivo(email);
	}

	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException, MailSendException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}

	@Transactional(readOnly = false)
	public void ativarCadastroPaciente(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));
		Usuario usuario = buscarUsuarioPorEmail(email);

		if (usuario.hasNotId()) {
			throw new AcessoNegadoException(
					"Houve um erro ao ativar sua conta! Entre em contato com a equipe de suporte!");
		}
		usuario.setAtivo(true);
		repository.save(usuario);

	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException, MailSendException {
		Usuario usuario = buscarPorEmailAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
		
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		usuario.setCodigoVerificador(verificador);
		
		///TODO TEST !!
		System.out.println(verificador);
		
		// emailService.enviarPedidoRedefinicaoDeSenha(email, verificador);
	}
}
