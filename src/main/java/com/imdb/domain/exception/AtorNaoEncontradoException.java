package com.imdb.domain.exception;

public class AtorNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public AtorNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public AtorNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de ator com código %d", id));
	}

}
