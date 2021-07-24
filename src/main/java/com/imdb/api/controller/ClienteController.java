package com.imdb.api.controller;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.imdb.api.assembler.ClienteInputDisassembler;
import com.imdb.api.assembler.GenericModelAssembler;
import com.imdb.api.model.ClienteModel;
import com.imdb.api.model.ClienteResumoModel;
import com.imdb.api.model.input.ClienteInput;
import com.imdb.api.openapi.controller.ClienteControllerOpenApi;
import com.imdb.domain.model.Cliente;
import com.imdb.domain.service.CadastroClienteService;

@RestController
@RequestMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteController implements ClienteControllerOpenApi {
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	
	@Autowired
	private GenericModelAssembler<ClienteResumoModel, Cliente> clienteResumoModelAssembler;
	
	@Autowired
	private GenericModelAssembler<ClienteModel, Cliente> clienteModelAssembler;
			
	@Autowired
	private ClienteInputDisassembler clienteInputDisassembler;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<ClienteResumoModel>> listar(@PageableDefault(size = 10) Pageable pageable){
		Page<Cliente> clientesPage = cadastroClienteService.listar(pageable);
		List<ClienteResumoModel> clientesResumoModel = clienteResumoModelAssembler.toCollectionModel(clientesPage.getContent(), ClienteResumoModel.class);
		Page<ClienteResumoModel> clientesResumoModelPage = new PageImpl<>(clientesResumoModel, pageable, clientesPage.getTotalElements());		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.body(clientesResumoModelPage);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteModel> buscar(@PathVariable Long id){
		Cliente cliente = cadastroClienteService.buscar(id);
		ClienteModel clienteModel = clienteModelAssembler.toModel(cliente, ClienteModel.class);
		return ResponseEntity.ok(clienteModel);
	}
	
	@PostMapping
	public ResponseEntity<ClienteModel> cadastrar(@RequestBody @Valid ClienteInput clienteInput){
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteInput);
		ClienteModel clienteModel = clienteModelAssembler.toModel(cadastroClienteService.salvar(cliente), ClienteModel.class);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(clienteModel.getId()).toUri();	
		return ResponseEntity.created(uri).body(clienteModel);	
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<ClienteModel> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteInput clienteInput){
		Cliente clienteAtual = cadastroClienteService.buscar(id);
		clienteInputDisassembler.copyToDomainObject(clienteInput, clienteAtual);
		clienteAtual = cadastroClienteService.salvar(clienteAtual);				
		ClienteModel clienteModel = clienteModelAssembler.toModel(clienteAtual, ClienteModel.class);
		return ResponseEntity.ok(clienteModel);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id){
		cadastroClienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}

}
