package com.mj.brewer.service;

import java.util.List;
import java.util.Optional;

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
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;
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

	@Transactional(readOnly = true)
	public Optional<Cidade> buscar(Long id) {
		return cidades.findById(id);
	}

	@Transactional
	public Cidade excluir(Long id) {
		Optional<Cidade> cidadeOp = buscar(id);

		if (!cidadeOp.isPresent())
			throw new ImpossivelExcluirEntidade("Cidade não encontrada!");

		try {
			cidades.delete(cidadeOp.get());
			cidades.flush();
			
			return cidadeOp.get();

		} catch (Exception e) {
			throw new ImpossivelExcluirEntidade(cidadeOp.get().getNome() + " não pode ser excluído!");
		}
	}

}
