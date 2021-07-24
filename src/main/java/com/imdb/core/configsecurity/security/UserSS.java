package com.imdb.core.configsecurity.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.imdb.domain.model.Perfil;

import lombok.Getter;

public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private Long id;
	
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();;
	
	public UserSS() {
	}
	
	public UserSS(Long id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(p -> new SimpleGrantedAuthority(p.getDescricao())).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return authorities;
	}

	@Override
	public String getPassword() {		
		return senha;
	}

	@Override
	public String getUsername() {		
		return email;
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
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}

}
