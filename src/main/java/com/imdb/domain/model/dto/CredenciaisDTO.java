package com.imdb.domain.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredenciaisDTO implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String senha;

}
