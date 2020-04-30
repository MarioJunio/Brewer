package com.mj.brewer.repository.helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.dto.VendaMes;
import com.mj.brewer.dto.VendaOrigem;
import com.mj.brewer.model.Cliente;
import com.mj.brewer.model.StatusVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mj.brewer.utils.Utils;
import com.mysql.jdbc.StringUtils;

public class VendasImpl implements VendasQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginationUtil;

	@SuppressWarnings("unchecked")
	@Override
	public PageImpl<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.createAlias("cliente", "cliente", JoinType.INNER_JOIN);
		criteria.createAlias("vendedor", "vendedor", JoinType.INNER_JOIN);

		paginationUtil.addPagination(criteria, pageable);
		adicionarFiltro(criteria, vendaFilter);

		return new PageImpl<Venda>(criteria.list(), pageable, total(vendaFilter));
	}

	private long total(VendaFilter vendaFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.setProjection(Projections.rowCount());

		adicionarFiltro(criteria, vendaFilter);

		return (long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Criteria criteria, VendaFilter vendaFilter) {

		LocalDateTime dataCriacaoInicio = vendaFilter.getDataCriacaoInicio() != null
				? LocalDateTime.of(vendaFilter.getDataCriacaoInicio(), LocalTime.MIN)
				: null;
		LocalDateTime dataCriacaoFinal = vendaFilter.getDataCriacaoFinal() != null
				? LocalDateTime.of(vendaFilter.getDataCriacaoFinal(), LocalTime.MAX)
				: null;

		// se o código foi informado
		if (vendaFilter.getId() != null)
			criteria.add(Restrictions.eq(Venda.ID, vendaFilter.getId()));

		// se o status foi informado
		if (vendaFilter.getStatus() != null)
			criteria.add(Restrictions.eq(Venda.STATUS_ENTREGA, vendaFilter.getStatus()));

		// se ambas as datas de criação inicio e final forem preenchidas
		if (vendaFilter.getDataCriacaoInicio() != null && vendaFilter.getDataCriacaoFinal() != null)
			criteria.add(Restrictions.and(Restrictions.ge(Venda.DATA_CRIACAO, dataCriacaoInicio),
					Restrictions.le(Venda.DATA_CRIACAO, dataCriacaoFinal)));
		else if (vendaFilter.getDataCriacaoInicio() != null) // se a data de criação inicial foi informada
			criteria.add(Restrictions.ge(Venda.DATA_CRIACAO, dataCriacaoInicio));
		else if (vendaFilter.getDataCriacaoFinal() != null) // se a data de criação final foi informada
			criteria.add(Restrictions.le(Venda.DATA_CRIACAO, dataCriacaoFinal));

		// se o valor total inicial e final forem preenchidos
		if (vendaFilter.getValorTotalInicial() != null && vendaFilter.getValorTotalFinal() != null)
			criteria.add(Restrictions.and(Restrictions.ge(Venda.VALOR_TOTAL, vendaFilter.getValorTotalInicial()),
					Restrictions.le(Venda.VALOR_TOTAL, vendaFilter.getValorTotalFinal())));
		else if (vendaFilter.getValorTotalInicial() != null) // se o valor inicial foi preenchido
			criteria.add(Restrictions.ge(Venda.VALOR_TOTAL, vendaFilter.getValorTotalInicial()));
		else if (vendaFilter.getValorTotalFinal() != null) // se o valor final foi preenchido
			criteria.add(Restrictions.le(Venda.VALOR_TOTAL, vendaFilter.getValorTotalFinal()));

		// se o nome do cliente foi preenchido
		if (!StringUtils.isNullOrEmpty(vendaFilter.getCliente()))
			criteria.add(Restrictions.ilike(Venda.CLIENTE, vendaFilter.getCliente(), MatchMode.START));

		// se o CPF/CNPJ foi informado
		if (!StringUtils.isNullOrEmpty(vendaFilter.getCpfCnpj())) {
			String cpfCnpj = Utils.removerMascaraCpfCnpj(vendaFilter.getCpfCnpj());
			criteria.add(Restrictions.ilike(Venda.CLIENTE.concat(".").concat(Cliente.CPF_CNPJ), cpfCnpj, MatchMode.START));
		}

	}

	@Override
	public Venda buscarComItens(Long id) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.add(Restrictions.eq(Venda.ID, id));
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);

		return (Venda) criteria.uniqueResult();
	}

	@Override
	public BigDecimal calcularVendasNoAno(int ano) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.setProjection(Projections.sum(Venda.VALOR_TOTAL));

		LocalDateTime date = LocalDateTime.now().withYear(ano);
		criteria.add(Restrictions.between(Venda.DATA_CRIACAO, date.with(TemporalAdjusters.firstDayOfYear()),
				date.with(TemporalAdjusters.lastDayOfYear())));

		criteria.add(Restrictions.eq(Venda.STATUS_ENTREGA, StatusVenda.EMITIDA));

		return (BigDecimal) Optional.ofNullable(criteria.uniqueResult()).orElse(BigDecimal.ZERO);
	}

	@Override
	public BigDecimal calcularVendasNoMes(int mes) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.setProjection(Projections.sum(Venda.VALOR_TOTAL));
		
		LocalDateTime date = LocalDateTime.now().withMonth(mes);
		criteria.add(Restrictions.between(Venda.DATA_CRIACAO, date.with(TemporalAdjusters.firstDayOfMonth()),
				date.with(TemporalAdjusters.lastDayOfMonth())));
		criteria.add(Restrictions.eq(Venda.STATUS_ENTREGA, StatusVenda.EMITIDA));
		
		return (BigDecimal) Optional.ofNullable(criteria.uniqueResult()).orElse(BigDecimal.ZERO);
	}

	@Override
	public BigDecimal calcularTickerMedio(int ano) {
		LocalDateTime date = LocalDateTime.now().withYear(ano);
		
		Query query = entityManager.createQuery(String.format("select sum(a.%s)/count(*) from Venda a where (a.%s between :data_inicial and :data_final) and (a.%s = :status)", Venda.VALOR_TOTAL, Venda.DATA_CRIACAO, Venda.STATUS_ENTREGA))
		.setParameter("data_inicial", date.with(TemporalAdjusters.firstDayOfYear()))
		.setParameter("data_final", date.with(TemporalAdjusters.lastDayOfYear()))
		.setParameter("status", StatusVenda.EMITIDA);
		
		return (BigDecimal) Optional.ofNullable(query.getSingleResult()).orElse(BigDecimal.ZERO);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaMes> vendasMeses() {
		return entityManager.createNamedQuery("Vendas.totalPorMes").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaOrigem> vendasOrigem() {
		return entityManager.createNamedQuery("Vendas.totalPorOrigem").getResultList();
	}

}
