package com.mballem.curso.security.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

	// @Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine template;

	public void enviarPedidoDeConfirmacaoDeCadastro(String destino, String codigo)
			throws MessagingException, MailSendException {
		//MimeMessage message = mailSender.createMimeMessage();
		//MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
		//		"UTF-8");

		Context context = new Context();

		context.setVariable("titulo", "Bem vindo a clinica Spring Security");
		context.setVariable("texto", "Confirme seu cadastro, clicando no link abaixo:");
		context.setVariable("linkConfirmacao", "http://localhost:8080/u/confirmacao/cadastro?codigo=" + codigo);

		String html = template.process("email/confirmacao", context);
		//helper.setTo(destino);
//		helper.setText(html, true);
//		helper.setSubject("Confirmação do cadastro - Clinica Spring security");
//		helper.setFrom("nao-responder@clinica.com.br");
//
//		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		//mailSender.send(message);
		
		System.out.println(html);
	}

	public void enviarPedidoRedefinicaoDeSenha(String destino, String codigoVerificador)
			throws MessagingException, MailSendException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();

		context.setVariable("titulo", "Redefinição de senha");
		context.setVariable("texto", "Para redefinir sua senha use o código de verificação abaixo, no formulário.");
		context.setVariable("verificador", codigoVerificador);

		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Redefinição de senha - Clinica Spring security");
		helper.setFrom("nao-responder@clinica.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);
	}

	public static boolean emailMatchesRegex(String email) {
		String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(regexPattern);
	}

}
