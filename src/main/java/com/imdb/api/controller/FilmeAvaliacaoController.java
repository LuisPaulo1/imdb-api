package com.imdb.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imdb.api.openapi.controller.FilmeAvaliacaoControllerOpenApi;
import com.imdb.domain.service.CadastroFilmeService;

@RestController
@RequestMapping(path = "/filmes/{filmeId}/avaliacoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class FilmeAvaliacaoController implements FilmeAvaliacaoControllerOpenApi {
	
	@Autowired
	private CadastroFilmeService cadastroFilmeService;
	
	@PreAuthorize("hasAnyRole('CLIENTE')")
	@PutMapping
	public ResponseEntity<Void> avaliarFilme(@PathVariable Long filmeId, @RequestParam(name = "voto") Integer voto){			
		cadastroFilmeService.avaliar(filmeId, voto);
		return ResponseEntity.noContent().build();
	}
	
}
