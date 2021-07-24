package com.imdb.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.imdb.api.model.input.ClienteInput;
import com.imdb.domain.model.Cliente;

@Component
public class ClienteInputDisassembler {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Cliente toDomainObject(ClienteInput clienteInput) {
		clienteInput.setSenha(pe.encode(clienteInput.getSenha()));		
		return modelMapper.map(clienteInput, Cliente.class);
	}
	
	public void copyToDomainObject(ClienteInput clienteInput, Cliente cliente) {
		clienteInput.setSenha(pe.encode(clienteInput.getSenha()));	
		modelMapper.map(clienteInput, cliente);
	}

}
