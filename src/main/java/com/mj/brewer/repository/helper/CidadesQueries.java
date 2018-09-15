package com.mj.brewer.repository.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Cidade;
import com.mj.brewer.model.filter.CidadeFilter;

public interface CidadesQueries {
	public Page<Cidade> filtrar(Pageable pageable, CidadeFilter cidadeFilter);
}
