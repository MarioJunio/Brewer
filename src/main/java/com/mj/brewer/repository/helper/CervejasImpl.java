package com.mj.brewer.repository.helper;

import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.dto.CervejaDTO;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.filter.CervejaFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mysql.jdbc.StringUtils;

public class CervejasImpl implements CervejasQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginationUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cerveja> filtrar(CervejaFilter cervejaFilter, Pageable pageable) {

		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cerveja.class);
		paginationUtil.addPagination(criteria, pageable);
		adicionarFiltro(cervejaFilter, criteria);

		return new PageImpl<>(criteria.list(), pageable, total(cervejaFilter).longValue());
	}

	private Number total(CervejaFilter cervejaFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cerveja.class);

		adicionarFiltro(cervejaFilter, criteria);

		criteria.setProjection(Projections.rowCount());

		return (Number) criteria.uniqueResult();
	}

	private void adicionarFiltro(CervejaFilter cervejaFilter, Criteria criteria) {

		if (cervejaFilter != null) {

			if (!StringUtils.isNullOrEmpty(cervejaFilter.getSku()))
				criteria.add(Restrictions.ilike(Cerveja.SKU, cervejaFilter.getSku(), MatchMode.START));

			if (!StringUtils.isNullOrEmpty(cervejaFilter.getNome()))
				criteria.add(Restrictions.ilike(Cerveja.NOME, cervejaFilter.getNome(), MatchMode.ANYWHERE));

			if (cervejaFilter.getEstilo() != null)
				criteria.add(Restrictions.eq(Cerveja.ESTILO, cervejaFilter.getEstilo()));

			if (cervejaFilter.getSabor() != null)
				criteria.add(Restrictions.eq(Cerveja.SABOR, cervejaFilter.getSabor()));

			if (cervejaFilter.getOrigem() != null)
				criteria.add(Restrictions.eq(Cerveja.ORIGEM, cervejaFilter.getOrigem()));

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CervejaDTO> filtrar(String nomeOuSKU) {
		String jpql = "select new com.mj.brewer.dto.CervejaDTO(a.id, a.nome, a.sku, a.origem, a.foto, a.valor) from Cerveja a where (lower(a.nome) like :filtro) or (lower(a.sku) like :filtro)";

		Query query = entityManager.createQuery(jpql, CervejaDTO.class);
		query.setParameter("filtro", nomeOuSKU.toLowerCase() + "%");

		return query.getResultList();
	}

	@Override
	public BigDecimal calcularValorTotalEstoque() {
		String jpql = "select sum(a.valor * a.quantidadeEstoque) from Cerveja a";

		return (BigDecimal) Optional.ofNullable(entityManager.createQuery(jpql, BigDecimal.class).getSingleResult())
				.orElse(BigDecimal.ZERO);
	}

	@Override
	public Long itensNoEstoque() {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cerveja.class);
		criteria.setProjection(Projections.sum(Cerveja.QUANTIDADE_ESTOQUE));
		return (Long) Optional.ofNullable(criteria.uniqueResult()).orElse(0L);
	}

	@Override
	public boolean subtrairEstoque(Cerveja cerveja, int estoqueAbater) {
		Query query = entityManager.createQuery("update Cerveja a set a.quantidadeEstoque = a.quantidadeEstoque - :quantidade_abater where a.id = :id");
		query.setParameter("quantidade_abater", estoqueAbater);
		query.setParameter("id", cerveja.getId());
		return query.executeUpdate() > 0;
	}

}
