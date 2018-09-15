package com.mj.brewer.repository.helper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.model.Estilo;
import com.mj.brewer.model.filter.EstiloFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mysql.jdbc.StringUtils;

public class EstilosImpl implements EstilosQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Estilo> filtrar(EstiloFilter estiloFilter, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Estilo.class);
		paginacaoUtil.addPagination(criteria, pageable);
		addFilter(criteria, estiloFilter);

		return new PageImpl<Estilo>(criteria.list(), pageable, total(estiloFilter));
	}

	private long total(EstiloFilter estiloFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Estilo.class);
		addFilter(criteria, estiloFilter);

		criteria.setProjection(Projections.count(Estilo.NOME));

		return ((Number) criteria.uniqueResult()).longValue();
	}

	private void addFilter(Criteria criteria, EstiloFilter estiloFilter) {

		if (!StringUtils.isNullOrEmpty(estiloFilter.getNome()))
			criteria.add(Restrictions.ilike(Estilo.NOME, estiloFilter.getNome(), MatchMode.START));
	}

}
