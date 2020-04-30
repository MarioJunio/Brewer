package com.mj.brewer.dto;

public class VendaMes {

	private String mes;
	private Integer quantidade;

	public VendaMes(String mes, Integer quantidade) {
		super();
		this.mes = mes;
		this.quantidade = quantidade;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return "VendaMes [mes=" + mes + ", quantidade=" + quantidade + "]";
	}

}
