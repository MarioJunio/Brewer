package com.mj.brewer.repository.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.mj.brewer.model.Grupo;
import com.mj.brewer.model.Usuario;
import com.mj.brewer.model.UsuarioGrupo;
import com.mj.brewer.model.filter.UsuarioFilter;
import com.mj.brewer.repository.pagination.PaginationUtil;
import com.mysql.jdbc.StringUtils;

public class UsuariosImpl implements UsuariosQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginationUtil paginationUtil;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> findByEmailAtivo(String email) {
		Query query = entityManager.createQuery("from Usuario u where u.email = :email and u.ativo = true", Usuario.class);
		query.setParameter("email", email);

		return query.getResultList().stream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> permissoes(Usuario usuario) {
		Query query = entityManager.createQuery(
				"select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class);
		query.setParameter("usuario", usuario);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public PageImpl<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		paginationUtil.addPagination(criteria, pageable);
		addFilter(criteria, usuarioFilter);

		List<Usuario> filtrados = criteria.list();
		filtrados.forEach(u -> Hibernate.initialize(u.getGrupos()));

		return new PageImpl<Usuario>(filtrados, pageable, total(usuarioFilter));
	}

	private long total(UsuarioFilter usuarioFilter) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
		addFilter(criteria, usuarioFilter);

		criteria.setProjection(Projections.count(Usuario.CODIGO));

		return ((Number) criteria.uniqueResult()).longValue();
	}

	private void addFilter(Criteria criteria, UsuarioFilter usuarioFilter) {

		if (usuarioFilter != null) {

			if (!StringUtils.isNullOrEmpty(usuarioFilter.getNome()))
				criteria.add(Restrictions.ilike(Usuario.NOME, usuarioFilter.getNome(), MatchMode.ANYWHERE));

			if (!StringUtils.isNullOrEmpty(usuarioFilter.getEmail()))
				criteria.add(Restrictions.ilike(Usuario.EMAIL, usuarioFilter.getEmail(), MatchMode.START));

			if (usuarioFilter.getGrupos() != null) {

				List<Criterion> subqueries = new ArrayList<>();
				for (Long codigo : usuarioFilter.getGrupos().stream().mapToLong(Grupo::getCodigo).toArray()) {
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UsuarioGrupo.class);
					detachedCriteria.add(Restrictions.eq("id.grupo.codigo", codigo));
					detachedCriteria.setProjection(Projections.property("id.usuario"));

					subqueries.add(Subqueries.propertyIn("codigo", detachedCriteria));
				}

				if (!subqueries.isEmpty()) {
					Criterion[] criterions = new Criterion[subqueries.size()];
					criteria.add(Restrictions.and(subqueries.toArray(criterions)));
				}
			}
		}

	}

	@Override
	public Usuario buscarComGrupos(Long id) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq(Usuario.CODIGO, id));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (Usuario) criteria.uniqueResult();
	}

}
