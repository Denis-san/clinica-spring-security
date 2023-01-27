package com.mballem.curso.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mballem.curso.security.service.UsuarioService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
	
	private static final String ADMIN = "ADMIN";
	private static final String MEDICO = "MEDICO";
	private static final String PACIENTE = "PACIENTE";

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize 
				.requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
				.requestMatchers("/", "/home").permitAll()
				.requestMatchers("/u/novo/cadastro", "/u/cadastro/realizado", "/u/cadastro/paciente/salvar").permitAll()
				.requestMatchers("/u/confirmacao/cadastro").permitAll()
				.requestMatchers("/u/p/**").permitAll()
				
				// acessos admin
				.requestMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(PACIENTE, MEDICO)
				.requestMatchers("/u/**").hasAuthority(ADMIN)
				.requestMatchers("/medicos/dados", "/medicos/editar", "/medicos/salvar").hasAnyAuthority(MEDICO, ADMIN)

				// acessos medicos
				.requestMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE, MEDICO)
				.requestMatchers("/medicos/**").hasAuthority(MEDICO)
				
				// acessos especialidades
				.requestMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
				.requestMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
				.requestMatchers("/especialidades/**").hasAuthority(ADMIN)

				// acesso pacientes
				.requestMatchers("/pacientes/**").hasAuthority(PACIENTE)
				
				.anyRequest().authenticated()
			)
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/", true)
			.failureUrl("/login-error")
			.permitAll()
		.and()
			.logout()
			.logoutSuccessUrl("/")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado")
		.and()
			.rememberMe();
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationMananger(HttpSecurity http, PasswordEncoder passwordEncoder, UsuarioService userDetailService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailService)
				.passwordEncoder(passwordEncoder)
				.and()
				.build();
	}

}
