package com.imdb.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoModel {
	
	@ApiModelProperty(example = "1")
	private Long id;	
	
	@ApiModelProperty(example = "4")
	private Integer voto;
	
	@ApiModelProperty(example = "25")
	private Long total;
	
}
