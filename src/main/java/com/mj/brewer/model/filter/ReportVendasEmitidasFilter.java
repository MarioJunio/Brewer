package com.mj.brewer.model.filter;

import java.time.LocalDate;

public class ReportVendasEmitidasFilter {

	private LocalDate dataInicio;
	private LocalDate dataFim;

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	@Override
	public String toString() {
		return "ReportVendasEmitidasFilter [dataInicio=" + dataInicio + ", dataFim=" + dataFim + "]";
	}

}
