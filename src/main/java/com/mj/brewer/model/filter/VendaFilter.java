package com.mj.brewer.model.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mj.brewer.model.StatusVenda;

public class VendaFilter {

	private Long id;
	private StatusVenda status;
	private LocalDate dataCriacaoInicio, dataCriacaoFinal;
	private BigDecimal valorTotalInicial, valorTotalFinal;
	private String cliente;
	private String cpfCnpj;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public LocalDate getDataCriacaoInicio() {
		return dataCriacaoInicio;
	}

	public void setDataCriacaoInicio(LocalDate dataCriacaoInicio) {
		this.dataCriacaoInicio = dataCriacaoInicio;
	}

	public LocalDate getDataCriacaoFinal() {
		return dataCriacaoFinal;
	}

	public void setDataCriacaoFinal(LocalDate dataCriacaoFinal) {
		this.dataCriacaoFinal = dataCriacaoFinal;
	}

	public BigDecimal getValorTotalInicial() {
		return valorTotalInicial;
	}

	public void setValorTotalInicial(BigDecimal valorTotalInicial) {
		this.valorTotalInicial = valorTotalInicial;
	}

	public BigDecimal getValorTotalFinal() {
		return valorTotalFinal;
	}

	public void setValorTotalFinal(BigDecimal valorTotalFinal) {
		this.valorTotalFinal = valorTotalFinal;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	@Override
	public String toString() {
		return "VendaFilter [id=" + id + ", status=" + status + ", dataCriacaoInicio=" + dataCriacaoInicio + ", dataCriacaoFinal="
				+ dataCriacaoFinal + ", valorTotalInicial=" + valorTotalInicial + ", valorTotalFinal=" + valorTotalFinal + ", cliente="
				+ cliente + ", cpfCnpj=" + cpfCnpj + "]";
	}

}
