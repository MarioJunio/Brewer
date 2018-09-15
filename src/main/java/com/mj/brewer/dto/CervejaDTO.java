package com.mj.brewer.dto;

import java.math.BigDecimal;

import com.mj.brewer.model.Origem;

public class CervejaDTO {

	private Long id;
	private String nome;
	private String sku;
	private String origem;
	private String foto;
	private BigDecimal valor;

	public CervejaDTO(Long id, String nome, String sku, Origem origem, String foto, BigDecimal valor) {
		super();
		this.id = id;
		this.nome = nome;
		this.sku = sku;
		this.origem = origem.getDescricao();
		this.foto = foto;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
