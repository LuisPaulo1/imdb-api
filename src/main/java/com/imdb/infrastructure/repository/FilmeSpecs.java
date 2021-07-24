package com.imdb.infrastructure.repository;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.imdb.api.model.filter.FilmeFilter;
import com.imdb.domain.model.Filme;

public class FilmeSpecs {
	public static Specification<Filme> usandoFiltro(FilmeFilter filtro) {
		return (root, query, builder) -> {
			
			if(Filme.class.equals(query.getResultType()))
				root.fetch("atores");				
			
			var predicates = new ArrayList<Predicate>();	
			
			if(StringUtils.hasText(filtro.getNome())) {
				predicates.add(builder.like(root.get("nome"), "%"+filtro.getNome()+"%"));
			}
			
			if(StringUtils.hasText(filtro.getGenero())) {
				predicates.add(builder.like(root.get("genero"), "%"+filtro.getGenero()+"%"));
			}
			
			if(StringUtils.hasText(filtro.getDiretor())) {
				predicates.add(builder.like(root.get("diretor"), "%"+filtro.getDiretor()+"%"));
			}
			
			if(StringUtils.hasText(filtro.getAtor())) {										
				predicates.add(builder.like(root.join("atores").get("nome"), "%"+filtro.getAtor()+"%"));
			}				
			
			query.orderBy(builder.asc(root.get("nome")));
			
			return builder.and(predicates.toArray(new Predicate[0]));
			
		};
	}
}
