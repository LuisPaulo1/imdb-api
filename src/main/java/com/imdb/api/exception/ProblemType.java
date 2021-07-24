package com.imdb.api.exception;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),	
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de Sistema"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ACESSO_NEGADO("/acesso-negado", "Acesso negado");	
	
	private String uri;
	private String title;
	
	ProblemType(String path, String title) {
		this.uri = "https://www.imdb.com" + path;
		this.title = title;
	}

}
