package com.mj.brewer.repository.pagination;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

	public void addPagination(Criteria criteria, Pageable pageable) {

		criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		criteria.setMaxResults(pageable.getPageSize());

		Sort sort = pageable.getSort();

		if (sort != Sort.unsorted()) {
			Sort.Order order = sort.iterator().next();
			String propriedadeOrdenacao = order.getProperty();

			criteria.addOrder(order.isAscending() ? Order.asc(propriedadeOrdenacao) : Order.desc(propriedadeOrdenacao));
		}

	}

}
