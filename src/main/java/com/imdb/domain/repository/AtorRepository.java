package com.imdb.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imdb.domain.model.Ator;

@Repository
public interface AtorRepository extends JpaRepository<Ator, Long> {

}
