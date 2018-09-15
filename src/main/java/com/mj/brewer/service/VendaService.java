package com.mj.brewer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.StatusVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;
import com.mj.brewer.repository.Vendas;

@Service
public class VendaService {

	@Autowired
	private Vendas vendas;

	@Transactional
	public Venda salvar(Venda venda) {
		venda.setDataCriacao(LocalDateTime.now());

		if (venda.getDataDaEntrega() != null)
			venda.setDataEntrega(
					LocalDateTime.of(venda.getDataDaEntrega(), Optional.ofNullable(venda.getHoraDaEntrega()).orElse(LocalTime.NOON)));

		return vendas.save(venda);
	}

	@Transactional(readOnly = true)
	public PageWrapper<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable, HttpServletRequest httpServletRequest) {
		return new PageWrapper<>(vendas.filtrar(vendaFilter, pageable), httpServletRequest);
	}

	@Transactional
	public Venda salvarEmitir(Venda venda) {
		venda.setStatusVenda(StatusVenda.EMITIDA);
		return salvar(venda);
	}

	@Transactional
	public Venda salvarEmail(Venda venda) {
		return salvar(venda);
	}

}
