package com.imdb.api.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_EMPTY)
@Getter
@Setter
public class FilmeDetalheModel {
	
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "O Falcão")
	private String nome;
	
	@ApiModelProperty(example = "The Falcon and the Winter Soldier")
	private String titulo;
	
	@ApiModelProperty(example = "2021-12-18")
	private LocalDate dataLancamento;
	
	@ApiModelProperty(example = "Ação, Aventura")
	private String genero;
	
	@ApiModelProperty(example = "Malcolm Spellman")
	private String diretor;
	
	private Set<AtorModel> atores;
	
	private List<AvaliacaoModel> avaliacoes;	
	
	@ApiModelProperty(example = "3.8")
	private Double mediaVotos;	
	
	private OffsetDateTime dataCadastro;		
	
	private OffsetDateTime dataAtualizacao;
	
}
