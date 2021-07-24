package com.imdb.api.model.input;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmeInput {
	
	@ApiModelProperty(example = "O Falcão", required = true)
	@Size(max = 100)
	@NotBlank
	private String nome;	
	
	@ApiModelProperty(example = "The Falcon and the Winter Soldier", required = true)
	@Size(max = 120)
	@NotBlank
	private String titulo;
	
	@ApiModelProperty(example = "2021-12-18", required = true)
	@NotNull
	private LocalDate dataLancamento;
	
	@ApiModelProperty(example = "Ação, Aventura", required = true)
	@Size(max = 30)
	@NotBlank
	private String genero;
	
	@ApiModelProperty(example = "Malcolm Spellman", required = true)
	@Size(max = 50)
	@NotBlank
	private String diretor;

}
