package com.autobots.automanager.adaptadores;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidades.Usuario;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final Usuario usuario;

	public UserDetailsImpl(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getId() {
		return usuario.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getPerfis().stream()
				.map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.name()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return usuario.getCredencial().getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getCredencial().getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}