package com.imdb.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imdb.core.configsecurity.security.UserSS;
import com.imdb.domain.exception.AuthorizationException;
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
		verificarPermissao(id);		
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id));
		return cliente;
	}
	
	@Transactional
	public Cliente salvar(Cliente cliente) {		
		if(cliente.getId() != null)
			verificarPermissao(cliente.getId());
		
		validarEnderecoDeEmail(cliente);	
			
		adicionarPerfil(cliente);
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public void excluir(Long id) {		
		verificarPermissao(id);		
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
	
	private void validarEnderecoDeEmail(Cliente cliente) {
		Cliente cli = clienteRepository.findByEmail(cliente.getEmail());
		if(cli != null)
			throw new NegocioException("Já existe um cadastro com o endereço de email informado.");
	}
	
	private void verificarPermissao(Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
	}
}
