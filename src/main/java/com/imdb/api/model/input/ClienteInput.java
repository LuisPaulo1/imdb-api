package com.imdb.api.model.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInput {
	
	@ApiModelProperty(example = "Bruna Cristina", required = true)
	@Size(max = 80)
	@NotBlank
	private String nome;
	
	@ApiModelProperty(example = "bruna@gmail.com", required = true)
	@Size(max = 50)
	@Email
	@NotBlank
	private String email;
	
	@Size(max = 10)
	@NotBlank
	private String senha;
	
	@ApiModelProperty(example = "ADMINISTRADOR", required = true)
	@NotNull
	private String tipo;

}
