package com.imdb.domain.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Avaliacao {
	
	public Avaliacao() {}
		
	public Avaliacao(Integer voto, Long total, Filme filme) {
		this.voto = voto;
		this.total = total;
		this.filme = filme;
	}

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	private Integer voto;
	
	private Long total;
	
	@ManyToOne
	@JoinColumn(name = "filme_id", foreignKey = @ForeignKey(name="fk_filme_avaliacao"), referencedColumnName="id")
	private Filme filme;
	
}
