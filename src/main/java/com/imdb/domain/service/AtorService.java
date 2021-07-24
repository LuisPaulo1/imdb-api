package com.imdb.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imdb.domain.exception.AtorNaoEncontradoException;
import com.imdb.domain.model.Ator;
import com.imdb.domain.repository.AtorRepository;

@Service
public class AtorService {
	
	@Autowired
	private AtorRepository atorRepository;
	
	public List<Ator> listar(){
		return atorRepository.findAll();
	}
	
	public Ator buscar(Long id) {
		Ator ator = atorRepository.findById(id)
				.orElseThrow(() -> new AtorNaoEncontradoException(id));
		return ator;
	}

}
