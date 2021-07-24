package com.imdb.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.imdb.domain.model.Filme;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long>, JpaSpecificationExecutor<Filme> {

}
