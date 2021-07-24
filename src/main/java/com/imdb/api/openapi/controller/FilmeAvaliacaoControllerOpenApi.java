package com.imdb.api.openapi.controller;

import org.springframework.http.ResponseEntity;

import com.imdb.api.exception.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Filmes")
public interface FilmeAvaliacaoControllerOpenApi {

    @ApiOperation("Avalia um filme por ID e voto")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Filme avaliado"),
        @ApiResponse(code = 400, message = "Valor do voto inválido", response = Problem.class),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "filme não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> avaliarFilme(Long filmeId, Integer voto);     
}