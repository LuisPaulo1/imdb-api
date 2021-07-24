package com.imdb.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Filme implements Comparable<Filme> {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String nome;

	@Column(nullable = false, length = 30)
	private String titulo;

	@Column(nullable = false)
	private LocalDate dataLancamento;

	@Column(nullable = false, length = 25)
	private String genero;

	@Column(nullable = false, length = 30)
	private String diretor;
	
	@Transient
	private Double mediaVotos;
	
	@Transient
	private Long totalVotos;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;		
	
	@UpdateTimestamp	
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	@OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
	private List<Avaliacao> avaliacoes = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "filme_ator", 
		joinColumns = @JoinColumn(name = "filme_id"), 
		inverseJoinColumns = @JoinColumn(name = "ator_id"))
	private Set<Ator> atores = new HashSet<>();
	
	public void calcularMedia() {		
		if(!avaliacoes.isEmpty()) {				    
			double media = (double) avaliacoes.stream().map(a -> a.getTotal()).reduce((x, y) -> x + y).get() / avaliacoes.size();
			this.mediaVotos = BigDecimal.valueOf(media).setScale(2, RoundingMode.HALF_UP).doubleValue();
		}else
			this.mediaVotos = 0d;
	}
	
	public void calcularVotosTotais() {
		if(!avaliacoes.isEmpty()) 
			this.totalVotos = avaliacoes.stream().map(a -> a.getTotal()).reduce((x, y) -> x + y).get();
		else
			this.totalVotos = 0L;		
	}
	
	public void adicionarAtores(Set<Ator> atores) {
		this.getAtores().addAll(atores);
	}
	
	public void removerAtores(Set<Ator> atores) {
		this.getAtores().removeAll(atores);
	}

	@Override
	public int compareTo(Filme obj) {
		return obj.getTotalVotos().compareTo(this.totalVotos);
	}
}