package com.imdb.domain.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cliente {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 80)
	private String nome;
	
	@Column(nullable = false, unique = true, length = 30)
	private String email;
	
	@Column(nullable = false, length = 10)
	private String senha;
	
	@Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
	
	@Column(nullable = false, length = 13)
	@Enumerated(EnumType.STRING)
	private TipoCliente tipo;
	
	@Column(nullable = false)
	private Integer perfil;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;		
	
	@UpdateTimestamp	
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	public void desativar() {
		setAtivo(false);
	}
	
	public Perfil getPerfil() {		
		return Perfil.toEnum(perfil);
	}
}