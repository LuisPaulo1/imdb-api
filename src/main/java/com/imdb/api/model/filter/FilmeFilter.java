package com.imdb.api.model.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilmeFilter {
	
	@ApiModelProperty(value = "Nome do filme")
	private String nome;	
	
	@ApiModelProperty(value = "Gênero do filme")
	private String genero;
	
	@ApiModelProperty(value = "Nome do diretor")
	private String diretor;
		
	@ApiModelProperty(value = "Nome do ator do filme")
	private String ator;

}
