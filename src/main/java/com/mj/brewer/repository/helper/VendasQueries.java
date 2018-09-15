package com.mj.brewer.repository.helper;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;

public interface VendasQueries {

	public PageImpl<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable);
	
}
