package com.mj.brewer.repository.helper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.dto.CervejaDTO;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.filter.CervejaFilter;

public interface CervejasQueries {

	Page<Cerveja> filtrar(CervejaFilter cervejaFilter, Pageable pageable);
	
	List<CervejaDTO> filtrar(String nomeOuSKU);
	
	BigDecimal calcularValorTotalEstoque();
	
	Long itensNoEstoque();
	
	boolean subtrairEstoque(Cerveja cerveja, int estoqueAbater);
	
}
