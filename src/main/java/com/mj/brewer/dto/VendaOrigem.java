package com.mj.brewer.dto;

public class VendaOrigem {

	private String mes;
	private Integer quantidadeNacional;
	private Integer quantidadeInternacional;

	public VendaOrigem() {
		super();
	}

	public VendaOrigem(String mes, Integer quantidadeNacional, Integer quantidadeInternacional) {
		super();
		this.mes = mes;
		this.quantidadeNacional = quantidadeNacional;
		this.quantidadeInternacional = quantidadeInternacional;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getQuantidadeNacional() {
		return quantidadeNacional;
	}

	public void setQuantidadeNacional(Integer quantidadeNacional) {
		this.quantidadeNacional = quantidadeNacional;
	}

	public Integer getQuantidadeInternacional() {
		return quantidadeInternacional;
	}

	public void setQuantidadeInternacional(Integer quantidadeInternacional) {
		this.quantidadeInternacional = quantidadeInternacional;
	}

}
