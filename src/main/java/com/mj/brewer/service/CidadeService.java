package com.mj.brewer.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Cidade;
import com.mj.brewer.model.filter.CidadeFilter;
import com.mj.brewer.repository.Cidades;
import com.mj.brewer.service.exception.CidadeJaCadastradaException;
import com.mj.brewer.utils.Utils;

@Service
public class CidadeService {

	@Autowired
	private Cidades cidades;

	@Transactional
	public Cidade salvar(Cidade cidade) {
		
		List<Cidade> cidadesEncontradas = cidades.findByNomeIgnoreCaseAndEstado(Utils.substituirCaracteresEspeciais(cidade.getNome()),
				cidade.getEstado());

		if (cidadesEncontradas != null && !cidadesEncontradas.isEmpty())
			throw new CidadeJaCadastradaException("Cidade já foi cadastrada!");

		return cidades.saveAndFlush(cidade);
	}

	public PageWrapper<Cidade> cidades(Pageable pageable, CidadeFilter cidadeFilter, HttpServletRequest httpServletRequest) {
		return new PageWrapper<>(cidades.filtrar(pageable, cidadeFilter), httpServletRequest);
	}

}
