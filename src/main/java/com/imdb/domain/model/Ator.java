package com.imdb.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Ator {
	
	@EqualsAndHashCode.Include	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 80)
	private String nome;
	
	@Column(nullable = false)
	private LocalDate dataNascimento;
	
	@Column(length = 200)
	private String biografia;
	
	@Column(nullable = false, length = 20)
	private String telefone;	
}
