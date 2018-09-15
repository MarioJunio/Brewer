package com.mj.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class PageWrapper<T> {

	public static final int DEFAULT_PAGE_SIZE = 15;
	
	private Page<T> page;
	private HttpServletRequest httpServletRequest;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		this.httpServletRequest = httpServletRequest;
	}

	public List<T> getContent() {
		return page.getContent();
	}

	public boolean isEmpty() {
		return getContent().isEmpty();
	}

	public int getTotalPages() {
		return page.getTotalPages();
	}

	public int getCurrent() {
		return page.getNumber();
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public String urlSort(String property) {
		String order = "";

		if (temOrdenacao(property))
			order = ascendente(property) ? "desc" : "asc";
		else
			order = "asc";

		return ServletUriComponentsBuilder.fromRequest(httpServletRequest)
				.replaceQueryParam("sort", String.format("%s,%s", property, order)).build(true).encode().toUriString();
	}

	public String url(int numeroPagina) {
		return ServletUriComponentsBuilder.fromRequest(httpServletRequest).replaceQueryParam("page", numeroPagina).build(true).encode()
				.toUriString();
	}

	public boolean temOrdenacao(String property) {
		return page.getSort() == Sort.unsorted() ? false : page.getSort().getOrderFor(property) != null;
	}

	public boolean ascendente(String property) {
		return page.getSort() == Sort.unsorted() ? false
				: (page.getSort().getOrderFor(property) != null ? page.getSort().getOrderFor(property).isAscending() : false);
	}

	public boolean descendente(String property) {
		return page.getSort() == Sort.unsorted() ? false
				: (page.getSort().getOrderFor(property) != null ? page.getSort().getOrderFor(property).isDescending() : false);
	}

}
