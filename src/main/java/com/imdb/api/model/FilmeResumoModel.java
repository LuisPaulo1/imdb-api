package com.imdb.api.model;

import java.time.LocalDate;
import java.util.Set;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmeResumoModel {
	
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
	
	private Set<AtorResumoModel> atores;
	
	@ApiModelProperty(example = "6")
	private Long totalVotos;

}
