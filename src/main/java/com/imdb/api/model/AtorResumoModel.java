package com.imdb.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtorResumoModel {
	
	@ApiModelProperty(example = "1")
	private Long id;	
	
	@ApiModelProperty(example = "Edward Norton")
	private String nome;
	
}
