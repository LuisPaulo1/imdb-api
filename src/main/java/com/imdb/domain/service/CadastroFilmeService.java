package com.imdb.domain.service;

import static com.imdb.infrastructure.repository.FilmeSpecs.usandoFiltro;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.imdb.api.model.filter.FilmeFilter;
import com.imdb.core.utils.URL;
import com.imdb.domain.exception.FilmeNaoEncontradoException;
import com.imdb.domain.exception.NegocioException;
import com.imdb.domain.model.Ator;
import com.imdb.domain.model.Avaliacao;
import com.imdb.domain.model.Filme;
import com.imdb.domain.repository.FilmeRepository;

@Service
public class CadastroFilmeService {
	
	@Autowired
	private FilmeRepository filmeRepository;
	
	@Autowired
	private AtorService atorService;
	
	public Page<Filme> pesquisar(FilmeFilter filmeFilter, Pageable pageable) {		
		Page<Filme> filmesPage = filmeRepository.findAll(usandoFiltro(filmeFilter), pageable);
		List<Filme> filmes = ordenarPorTotalDeVotos(filmesPage.getContent());
		return new PageImpl<>(filmes);
	}
	
	private List<Filme> ordenarPorTotalDeVotos(List<Filme> filmes){		
		 filmes.forEach(filme -> filme.calcularVotosTotais());
		 return filmes.stream().sorted((t1, t2) -> t1.compareTo(t2)).collect(Collectors.toList());		 
	}
	
	public Filme buscar(Long filmeId) {
		Filme filme = filmeRepository.findById(filmeId)
				.orElseThrow(() -> new FilmeNaoEncontradoException(filmeId));
		filme.calcularMedia();
		return filme;
	}	
	
	@Transactional
	public Filme salvar(Filme filme, String idsAtoresAdicionar) {		
		Set<Ator> atores = buscarAtores(idsAtoresAdicionar);
		filme.adicionarAtores(atores);		
		return filmeRepository.save(filme);		
	}
	
	@Transactional
	public Filme salvar(Filme filme, String idsAtoresAdicionar, String idsAtoresRemover) {
		
		if(StringUtils.hasText(idsAtoresAdicionar)) {
			Set<Ator> atores = buscarAtores(idsAtoresAdicionar);
			filme.adicionarAtores(atores);			
		}
		
		if(StringUtils.hasText(idsAtoresRemover)) {
			Set<Ator> atores = buscarAtores(idsAtoresRemover);
			filme.removerAtores(atores);	
		}
		
		return filmeRepository.save(filme);
	}
	
	private Set<Ator> buscarAtores(String idsAtores) {
		Set<Long> ids = URL.decodeLongSet(idsAtores);		
		Set<Ator> atores = new HashSet<>();
		ids.forEach(id -> {
			atores.add(atorService.buscar(id));
		});
		return atores;
	}
	
	@Transactional
	public void avaliar(Long filmeId, Integer voto) {
		if(!(voto >= 0 && voto <= 4))
			throw new NegocioException("O valor do voto deve ser entre 0 e 4");
		
		Filme filme = buscar(filmeId);
		
		if(filme.getAvaliacoes().isEmpty()) {			
			filme.getAvaliacoes().add(new Avaliacao(voto, 1L, filme));			
		}else {
			Optional<Avaliacao> resultado = filme.getAvaliacoes().stream().filter(a -> a.getVoto().equals(voto)).findFirst();		    
			if(resultado.isPresent()) {
		    	Avaliacao avaliacao = resultado.get();
		    	avaliacao.setTotal(avaliacao.getTotal() + 1);		    	
		    	filme.getAvaliacoes().add(avaliacao);
		    }else {
		    	filme.getAvaliacoes().add(new Avaliacao(voto, 1L, filme));
		    }
			
		}		
	}	
}
