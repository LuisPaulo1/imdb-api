package com.imdb.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.imdb.api.exception.Problem;
import com.imdb.api.model.ClienteModel;
import com.imdb.api.model.ClienteResumoModel;
import com.imdb.api.model.input.ClienteInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Clientes")
public interface ClienteControllerOpenApi {

    @ApiOperation("Lista os clientes")
    @ApiResponses({       
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class)        
    })
    ResponseEntity<Page<ClienteResumoModel>> listar(Pageable pageable);

    @ApiOperation("Busca um cliente por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do cliente inválido", response = Problem.class),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "Cliente não encontrado", response = Problem.class)
    })
    ResponseEntity<ClienteModel> buscar(@ApiParam(value = "ID do cliente") Long clienteId);

    @ApiOperation("Cadastra um cliente")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Cliente cadastrado"),
    })
    ResponseEntity<ClienteModel> cadastrar(ClienteInput clienteInput);
    
    @ApiOperation("Atualiza um cliente por ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cliente atualizado"),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "Cliente não encontrado", response = Problem.class)
    })
    ResponseEntity<ClienteModel> atualizar(@ApiParam(value = "ID do cliente") Long clienteId, ClienteInput clienteInput);

    @ApiOperation("Exclui um cliente por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cliente excluído"),
        @ApiResponse(code = 403, message = "Acesso negado", response = Problem.class),
        @ApiResponse(code = 404, message = "Cliente não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> remover(@ApiParam(value = "ID do cliente") Long id);
}