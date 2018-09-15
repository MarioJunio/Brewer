package com.mj.brewer.repository.helper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.model.Cliente;
import com.mj.brewer.model.filter.ClienteFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mj.brewer.utils.Utils;
import com.mysql.jdbc.StringUtils;

public class ClientesImpl implements ClientesQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginationUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cliente> filtrar(ClienteFilter clienteFilter, Pageable pageable) {

		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cliente.class);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);

		paginationUtil.addPagination(criteria, pageable);
		adicionarFiltro(clienteFilter, criteria);

		return new PageImpl<Cliente>(criteria.list(), pageable, total(clienteFilter).longValue());
	}

	private Number total(ClienteFilter clienteFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cliente.class);

		adicionarFiltro(clienteFilter, criteria);

		criteria.setProjection(Projections.rowCount());

		return (Number) criteria.uniqueResult();
	}

	private void adicionarFiltro(ClienteFilter clienteFilter, Criteria criteria) {

		if (clienteFilter != null) {

			if (!StringUtils.isNullOrEmpty(clienteFilter.getNome()))
				criteria.add(Restrictions.ilike(Cliente.NOME, clienteFilter.getNome(), MatchMode.ANYWHERE));

			if (!StringUtils.isNullOrEmpty(clienteFilter.getCpfCnpj()))
				criteria.add(Restrictions.eq(Cliente.CPF_CNPJ, Utils.removerMascaraCpfCnpj(clienteFilter.getCpfCnpj())));

		}

	}

}
