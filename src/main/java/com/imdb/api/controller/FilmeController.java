package com.imdb.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.imdb.api.assembler.GenericInputDisassembler;
import com.imdb.api.assembler.GenericModelAssembler;
import com.imdb.api.model.FilmeDetalheModel;
import com.imdb.api.model.FilmeModel;
import com.imdb.api.model.FilmeResumoModel;
import com.imdb.api.model.filter.FilmeFilter;
import com.imdb.api.model.input.FilmeInput;
import com.imdb.api.openapi.controller.FilmeControllerOpenApi;
import com.imdb.domain.model.Filme;
import com.imdb.domain.service.CadastroFilmeService;

@RestController
@RequestMapping(path = "/filmes", produces = MediaType.APPLICATION_JSON_VALUE)
public class FilmeController implements FilmeControllerOpenApi {
	
	@Autowired
	private CadastroFilmeService filmeService;
	
	@Autowired
	private GenericModelAssembler<FilmeResumoModel, Filme> filmeResumoModelAssembler;
		
	@Autowired
	private GenericModelAssembler<FilmeDetalheModel, Filme> filmeDetalheModelAssembler;
	
	@Autowired
	private GenericModelAssembler<FilmeModel, Filme> filmeModelAssembler;
	
	@Autowired
	private GenericInputDisassembler<FilmeInput, Filme> filmeInputDisassembler; 
	
	@GetMapping
	public ResponseEntity<Page<FilmeResumoModel>> pesquisar(FilmeFilter filmeFilter, @PageableDefault(size = 10) Pageable pageable){
		Page<Filme> filmesPage = filmeService.pesquisar(filmeFilter, pageable);
		List<FilmeResumoModel> filmesResumoModel = filmeResumoModelAssembler.toCollectionModel(filmesPage.getContent(), FilmeResumoModel.class);
		Page<FilmeResumoModel> filmesResumoModelPage = new PageImpl<>(filmesResumoModel, pageable, filmesPage.getTotalElements());		
		return ResponseEntity.ok(filmesResumoModelPage);
	}
	
	@GetMapping("/{filmeId}")
	public ResponseEntity<FilmeDetalheModel> consultarFilmeComDetalhes(@PathVariable Long filmeId){
		Filme filme = filmeService.buscar(filmeId);					
		FilmeDetalheModel filmeDetalheModel = filmeDetalheModelAssembler.toModel(filme, FilmeDetalheModel.class);			
		return ResponseEntity.ok(filmeDetalheModel);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<FilmeModel> adicionar(@RequestBody @Valid FilmeInput filmeInput, @RequestParam String idsAtores){		
		Filme filme = filmeInputDisassembler.toDomainObject(filmeInput, Filme.class);		
		FilmeModel filmeModel = filmeModelAssembler.toModel(filmeService.salvar(filme, idsAtores), FilmeModel.class);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(filmeModel.getId()).toUri();	
		return ResponseEntity.created(uri).body(filmeModel);
	}	
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{filmeId}")
	public ResponseEntity<FilmeModel> atualizar(@PathVariable Long filmeId, @RequestBody @Valid FilmeInput filmeInput, 
			@RequestParam(required = false) String idsAtoresAdicionar, @RequestParam(required = false) String idsAtoresRemover){
		Filme filmeAtual = filmeService.buscar(filmeId);
		filmeInputDisassembler.copyToDomainObject(filmeInput, filmeAtual);
		filmeAtual = filmeService.salvar(filmeAtual, idsAtoresAdicionar, idsAtoresRemover);
		FilmeModel filmeModel = filmeModelAssembler.toModel(filmeAtual, FilmeModel.class);
		return ResponseEntity.ok(filmeModel);
	}
	
}
