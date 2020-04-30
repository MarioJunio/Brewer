package com.mj.brewer.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Estilo;
import com.mj.brewer.model.filter.EstiloFilter;
import com.mj.brewer.repository.Estilos;
import com.mj.brewer.service.exception.EstiloCadastradoException;
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Service
public class EstiloService {

	@Autowired
	private Estilos estiloRep;

	@Transactional
	public Estilo salvar(Estilo estilo) {

		if (estiloRep.findByNomeIgnoreCase(estilo.getNome()).isPresent()) {
			throw new EstiloCadastradoException("Estilo já cadastrado!");
		}

		return estiloRep.saveAndFlush(estilo);
	}

	public PageWrapper<Estilo> pesquisa(EstiloFilter estiloFilter, Pageable pageable, HttpServletRequest httpServletRequest) {
		return new PageWrapper<>(estiloRep.filtrar(estiloFilter, pageable), httpServletRequest);
	}

	@Transactional
	public void excluir(Estilo estilo) {

		try {
			estiloRep.delete(estilo);
			estiloRep.flush();
		} catch (Exception e) {
			throw new ImpossivelExcluirEntidade(estilo.getNome() + " não é possível ser excluído!");
		}
	}

	@Transactional(readOnly = true)
	public Optional<Estilo> buscar(Long id) {
		return estiloRep.findById(id);
	}

}
