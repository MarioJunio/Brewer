package com.mj.brewer.repository.helper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.dto.VendaMes;
import com.mj.brewer.dto.VendaOrigem;
import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;

public interface VendasQueries {

	PageImpl<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable);
	
	Venda buscarComItens(Long id);
	
	BigDecimal calcularVendasNoAno(int ano);
	
	BigDecimal calcularVendasNoMes(int mes);
	
	BigDecimal calcularTickerMedio(int ano);
	
	List<VendaMes> vendasMeses();
	
	List<VendaOrigem> vendasOrigem();
}
