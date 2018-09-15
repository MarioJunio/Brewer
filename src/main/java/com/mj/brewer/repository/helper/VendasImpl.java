package com.mj.brewer.repository.helper;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mj.brewer.model.Cliente;
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

}
