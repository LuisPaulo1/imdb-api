package com.imdb.infrastructure.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.imdb.core.configsecurity.security.UserSS;
import com.imdb.domain.model.Cliente;
import com.imdb.domain.model.Perfil;
import com.imdb.domain.repository.ClienteRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = repo.findByEmail(email);
		if (cliente == null) {
			throw new UsernameNotFoundException(email);
		}
		Set<Perfil> perfis = new HashSet<>();		
		perfis.add(cliente.getPerfil());
				
		return new UserSS(cliente.getId(), cliente.getEmail(), cliente.getSenha(), perfis);
	}
}