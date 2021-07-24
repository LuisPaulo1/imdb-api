package com.imdb.api.model;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtorModel {
	
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Edward Norton")
	private String nome;
	
	@ApiModelProperty(example = "1969-10-18")
	private LocalDate dataNascimento;
	
	@ApiModelProperty(example = "O ator, cineasta e ativista norte-americano")
	private String biografia;
	
	@ApiModelProperty(example = "55-966327499")
	private String telefone;

}
