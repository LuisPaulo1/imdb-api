package com.imdb.api.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imdb.api.assembler.GenericModelAssembler;
import com.imdb.api.model.AtorModel;
import com.imdb.api.model.AtorResumoModel;
import com.imdb.api.openapi.controller.AtorControllerOpenApi;
import com.imdb.domain.model.Ator;
import com.imdb.domain.service.AtorService;

@RestController
@RequestMapping(path = "/atores", produces = MediaType.APPLICATION_JSON_VALUE)
public class AtorController implements AtorControllerOpenApi {
	
	@Autowired
	private AtorService atorService;
	
	@Autowired
	private GenericModelAssembler<AtorModel, Ator> atorModelAssembler;
	
	@Autowired
	private GenericModelAssembler<AtorResumoModel, Ator> atorResumoModelAssembler;
	
	@GetMapping
	public ResponseEntity<List<AtorResumoModel>> listar(){		
		List<AtorResumoModel> atoresResumoModel = atorResumoModelAssembler.toCollectionModel(atorService.listar(), AtorResumoModel.class);
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES))
				.body(atoresResumoModel);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AtorModel> buscar(@PathVariable Long id){
		Ator ator = atorService.buscar(id);
		AtorModel atorModel = atorModelAssembler.toModel(ator, AtorModel.class);
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES))
				.body(atorModel);
	}

}
