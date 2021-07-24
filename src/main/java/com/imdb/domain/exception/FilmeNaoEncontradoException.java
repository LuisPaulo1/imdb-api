package com.imdb.domain.exception;

public class FilmeNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public FilmeNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public FilmeNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de filme com código %d", id));
	}

}
