package com.autobots.automanager.filtros;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Autenticador extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager gerenciadorAutenticacao;
	private ProvedorJwt provedorJwt;

	public Autenticador(AuthenticationManager gerenciadorAutenticacao, ProvedorJwt provedorJwt) {
		this.gerenciadorAutenticacao = gerenciadorAutenticacao;
		this.provedorJwt = provedorJwt;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		Credencial credencial = null;
		try {
			credencial = new ObjectMapper().readValue(request.getInputStream(), Credencial.class);
		} catch (IOException e) {
			credencial = new Credencial();
			credencial.setLogin("");
			credencial.setSenha("");
		}
		UsernamePasswordAuthenticationToken dadosAutenticacao = new UsernamePasswordAuthenticationToken(
				credencial.getLogin(), credencial.getSenha(), new ArrayList<>());
		Authentication autenticacao = gerenciadorAutenticacao.authenticate(dadosAutenticacao);
		return autenticacao;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication autenticacao) throws IOException, ServletException {
		UserDetails usuario = (UserDetails) autenticacao.getPrincipal();
		String nomeUsuario = usuario.getUsername();
		String jwt = provedorJwt.proverJwt(nomeUsuario);
		response.addHeader("Authorization", "Bearer " + jwt);
	}
}