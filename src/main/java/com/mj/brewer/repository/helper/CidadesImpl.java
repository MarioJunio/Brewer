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

import com.mj.brewer.model.Cidade;
import com.mj.brewer.model.filter.CidadeFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mysql.jdbc.StringUtils;

public class CidadesImpl implements CidadesQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginationUtil;

	private Class<Cidade> clazz = Cidade.class;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cidade> filtrar(Pageable pageable, CidadeFilter cidadeFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(clazz);
		criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);

		paginationUtil.addPagination(criteria, pageable);
		adicionarFiltro(criteria, cidadeFilter);

		return new PageImpl<Cidade>(criteria.list(), pageable, total(cidadeFilter).longValue());
	}

	public Number total(CidadeFilter cidadeFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		adicionarFiltro(criteria, cidadeFilter);

		return (Number) criteria.uniqueResult();
	}

	public void adicionarFiltro(Criteria criteria, CidadeFilter cidadeFilter) {

		if (cidadeFilter.getEstado() != null)
			criteria.add(Restrictions.eq("estado", cidadeFilter.getEstado()));

		if (!StringUtils.isNullOrEmpty(cidadeFilter.getNome()))
			criteria.add(Restrictions.ilike("nome", cidadeFilter.getNome(), MatchMode.START));

	}
}
