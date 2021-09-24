package com.imdb.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imdb.domain.exception.ClienteNaoEncontradoException;
import com.imdb.domain.exception.NegocioException;
import com.imdb.domain.model.Cliente;
import com.imdb.domain.model.Perfil;
import com.imdb.domain.repository.ClienteRepository;

@Service
public class CadastroClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Page<Cliente> listar(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}
	
	public Cliente buscar(Long id) {		
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id));
		return cliente;
	}
	
	@Transactional
	public Cliente salvar(Cliente cliente) {					
		adicionarPerfil(cliente);
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public void excluir(Long id) {	
		Cliente cliente = buscar(id);
		cliente.desativar();				
	}
	
	private void adicionarPerfil(Cliente cliente) {
		if(cliente.getTipo() == null)
			throw new NegocioException("Tipo de cliente inválido. Informe ADMINISTRADOR OU USUARIO.");
					
		if(cliente.getTipo().name().equals("ADMINISTRADOR")) 
			cliente.setPerfil(Perfil.ADMIN.getCod());
		else
			cliente.setPerfil(Perfil.CLIENTE.getCod());
	}	
}
