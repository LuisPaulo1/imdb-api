package com.imdb.api.model;

import java.time.OffsetDateTime;

import com.imdb.domain.model.TipoCliente;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModel {
	
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Bruna Cristina")
	private String nome;
	
	@ApiModelProperty(example = "bruna@gmail.com")
	private String email;
		
	private Boolean ativo;
	
	@ApiModelProperty(example = "ADMINISTRADOR")
	private TipoCliente tipo;
	
	private OffsetDateTime dataCadastro;		
	
	private OffsetDateTime dataAtualizacao;

}
