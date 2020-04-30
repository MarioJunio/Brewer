package com.mj.brewer.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.dto.CervejaDTO;
import com.mj.brewer.event.holder.OnDeleteCervejaEvent;
import com.mj.brewer.event.holder.OnSaveCervejaEvent;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.filter.CervejaFilter;
import com.mj.brewer.repository.Cervejas;
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Service
public class CervejaService {

	@Autowired
	private Cervejas cervejas;

	@Autowired
	public ApplicationEventPublisher eventPublisher;

	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);
		eventPublisher.publishEvent(new OnSaveCervejaEvent(cerveja));
	}

	public PageWrapper<Cerveja> cervejas(CervejaFilter cervejaFilter, Pageable pageable, HttpServletRequest httpServletRequest) {
		return new PageWrapper<Cerveja>(cervejas.filtrar(cervejaFilter, pageable), httpServletRequest);
	}

	public List<CervejaDTO> cervejas(String nomeOuSKU) {
		return cervejas.filtrar(nomeOuSKU);
	}

	@Transactional
	public void excluir(Cerveja cerveja) {
		
		try {
			cervejas.delete(cerveja);
			cervejas.flush();
			eventPublisher.publishEvent(new OnDeleteCervejaEvent(cerveja));
		} catch (Exception e) {
			throw new ImpossivelExcluirEntidade(
					String.format("Não foi possível excluir \'%s\', pois ela já foi vendida!", cerveja.getNome()));
		}
	}
	
	@Transactional(readOnly = true)
	public BigDecimal valorTotalEstoque() {
		return cervejas.calcularValorTotalEstoque();
	}
	
	@Transactional(readOnly = true)
	public Long itensNoEstoque() {
		return cervejas.itensNoEstoque();
	}
	
	@Transactional
	public boolean subtrairEstoque(Cerveja cerveja, int estoqueAbater) {
		return cervejas.subtrairEstoque(cerveja, estoqueAbater);
	}
}
