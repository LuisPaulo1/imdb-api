package com.imdb.api.model;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class FilmeModel {
	
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
	
}
