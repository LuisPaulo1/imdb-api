package com.imdb.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.imdb.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query(value = "from Cliente u where u.ativo = true and u.tipo = 'USUARIO' order by u.nome")
	Page<Cliente> findAll(Pageable pageable);
	
	Cliente findByEmail(String email);
}
