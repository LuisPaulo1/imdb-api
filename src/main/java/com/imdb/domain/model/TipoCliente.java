package com.imdb.domain.model;

public enum TipoCliente {
	
	ADMINISTRADOR("Administrador"),
	USUARIO("Usuário");
		
	private String descrição;
	
	private TipoCliente(String descricao) {		
		this.descrição = descricao;
	}	
	
	public String getDescrição() {
		return descrição;
	}	

}
