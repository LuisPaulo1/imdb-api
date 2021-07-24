package com.imdb.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.imdb.api.exception.Problem;
import com.imdb.api.model.FilmeDetalheModel;
import com.imdb.api.model.FilmeModel;
import com.imdb.api.model.FilmeResumoModel;
import com.imdb.api.model.filter.FilmeFilter;
import com.imdb.api.model.input.FilmeInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Filmes")
public interface FilmeControllerOpenApi {

    @ApiOperation("Lista os filmes")
    ResponseEntity<Page<FilmeResumoModel>> pesquisar(FilmeFilter filmeFilter, Pageable pageable);

    @ApiOperation("Busca um filme por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do filme inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Filme não encontrado", response = Problem.class)
    })
    ResponseEntity<FilmeDetalheModel> consultarFilmeComDetalhes(@ApiParam(value = "ID do filme") Long filmeId);

    @ApiOperation("Cadastra um filme")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Filme cadastrado"),
        @ApiResponse(code = 400, message = "Valor do parâmetro inválido", response = Problem.class),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "Ator não encontrado", response = Problem.class)
    })
    ResponseEntity<FilmeModel> adicionar(FilmeInput filmeInput, @ApiParam(value = "ID(s) do(s) ator(es). Ex: 1 ou 1,2") String idsAtores);
    
    @ApiOperation("Atualiza um filme por ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Filme atualizado"),
        @ApiResponse(code = 400, message = "Valor do parâmetro inválido", response = Problem.class),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "Filme não encontrado", response = Problem.class)
    })
    public ResponseEntity<FilmeModel> atualizar(@ApiParam(value = "ID do filme") Long filmeId, FilmeInput filmeInput, 
    		@ApiParam(value = "ID(s) do(s) ator(es) para adicionar. Ex: 1 ou 1,2") String idsAtoresAdicionar, 
    		@ApiParam(value = "ID(s) do(s) ator(es) para remover. Ex: 1 ou 1,2") String idsAtoresRemover);
}