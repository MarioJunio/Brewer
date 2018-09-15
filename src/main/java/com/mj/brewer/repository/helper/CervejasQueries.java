package com.mj.brewer.repository.helper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.dto.CervejaDTO;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.filter.CervejaFilter;

public interface CervejasQueries {

	public Page<Cerveja> filtrar(CervejaFilter cervejaFilter, Pageable pageable);
	
	public List<CervejaDTO> filtrar(String nomeOuSKU);
	
}
