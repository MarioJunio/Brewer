package com.mj.brewer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.dto.VendaMes;
import com.mj.brewer.dto.VendaOrigem;
import com.mj.brewer.event.holder.OnVendaSaveEvent;
import com.mj.brewer.model.StatusVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;
import com.mj.brewer.repository.Vendas;

@Service
public class VendaService {

	@Autowired
	private Vendas vendas;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Transactional
	public Venda salvar(Venda venda) {

		// se estiver editandk uma venda
		if (venda.isEdicao()) {
			Venda oldVenda = vendas.findById(venda.getId()).get();
			venda.setDataCriacao(oldVenda.getDataCriacao());
		} else
			venda.setDataCriacao(LocalDateTime.now());

		if (venda.getDataDaEntrega() != null)
			venda.setDataEntrega(
					LocalDateTime.of(venda.getDataDaEntrega(), Optional.ofNullable(venda.getHoraDaEntrega()).orElse(LocalTime.NOON)));

		// pega a venda com o seu id
		venda = vendas.save(venda);

		// dispara evento de venda salva
		eventPublisher.publishEvent(new OnVendaSaveEvent(venda));

		return venda;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	public void cancelar(Venda venda) {
		vendas.save(venda);
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

	@Transactional(readOnly = true)
	public Venda buscarComItens(Long id) {
		return vendas.buscarComItens(id);
	}

	@Transactional(readOnly = true)
	public BigDecimal vendasNoAno(int ano) {
		return vendas.calcularVendasNoAno(ano);
	}

	@Transactional(readOnly = true)
	public BigDecimal vendasNoMes(int mes) {
		return vendas.calcularVendasNoMes(mes);
	}

	@Transactional(readOnly = true)
	public BigDecimal ticketMedio(int ano) {
		return vendas.calcularTickerMedio(ano);
	}

	@Transactional(readOnly = true)
	public Optional<Venda> buscar(Long vendaId) {
		return vendas.findById(vendaId);
	}

	@Transactional(readOnly = true)
	public List<VendaMes> chartVendasMeses() {
		List<VendaMes> listVendasMeses = vendas.vendasMeses();

		if (!listVendasMeses.isEmpty()) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
			YearMonth ultimo = null;

			for (int i = 0; i < 6; i++) {

				if (i < listVendasMeses.size()) {
					VendaMes vm = listVendasMeses.get(i);
					YearMonth atual = YearMonth.parse(vm.getMes(), formatter);

					// se não é o primeiro elemento da iteracao
					if (ultimo != null) {
						int mesAnterior = ultimo.getMonthValue() - 1;

						// se o mes anterior do ultimo mes obtido for diferente do mes atual, será
						// adicionado o mes anterior do ultimo mes a lista
						if (mesAnterior != atual.getMonthValue()) {
							ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
							listVendasMeses.add(i, new VendaMes(ultimo.format(formatter), 0));
						} else {
							ultimo = atual;
						}

					} else {
						ultimo = atual;
					}

				} else {
					int mesAnterior = ultimo.getMonthValue() - 1;
					ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
					listVendasMeses.add(i, new VendaMes(ultimo.format(formatter), 0));
				}
			}

			listVendasMeses = listVendasMeses.subList(0, 6);
		}

		return listVendasMeses;
	}

	@Transactional(readOnly = true)
	public List<VendaOrigem> chartVendasOrigem() {
		List<VendaOrigem> listVendasOrigem = vendas.vendasOrigem();

		if (!listVendasOrigem.isEmpty()) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
			YearMonth ultimo = null;

			for (int i = 0; i < 6; i++) {

				// se a quantidade de meses retornados for menor que 6 adicione os subsequentes para completar o gráfico
				if (i < listVendasOrigem.size()) {
					VendaOrigem vm = listVendasOrigem.get(i);
					YearMonth atual = YearMonth.parse(vm.getMes(), formatter);

					// se não é o primeiro elemento da iteracao
					if (ultimo != null) {
						int mesAnterior = ultimo.getMonthValue() - 1;

						// se o mes anterior do ultimo mes obtido for diferente do mes atual, será
						// adicionado o mes anterior do ultimo mes a lista
						if (mesAnterior != atual.getMonthValue()) {
							ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
							listVendasOrigem.add(i, new VendaOrigem(ultimo.format(formatter), 0, 0));
						} else {
							ultimo = atual;
						}

					} else {
						ultimo = atual;
					}

				} else {
					int mesAnterior = ultimo.getMonthValue() - 1;
					ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
					listVendasOrigem.add(i, new VendaOrigem(ultimo.format(formatter), 0, 0));
				}
			}

			listVendasOrigem = listVendasOrigem.subList(0, 6);
		}

		return listVendasOrigem;
	}

}
