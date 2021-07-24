package com.imdb.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.imdb.api.exception.Problem;
import com.imdb.api.model.AtorModel;
import com.imdb.api.model.AtorResumoModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Atores")
public interface AtorControllerOpenApi {

    @ApiOperation("Lista os atores")
    ResponseEntity<List<AtorResumoModel>> listar();

    @ApiOperation("Busca um ator por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do ator inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Ator não encontrado", response = Problem.class)
    })
    ResponseEntity<AtorModel> buscar(@ApiParam(value = "ID do ator") Long atorId);  
}