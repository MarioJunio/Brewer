package com.mj.brewer.repository.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Estilo;
import com.mj.brewer.model.filter.EstiloFilter;

public interface EstilosQueries {
	
	Page<Estilo> filtrar(EstiloFilter estiloFilter, Pageable pageable);
}
