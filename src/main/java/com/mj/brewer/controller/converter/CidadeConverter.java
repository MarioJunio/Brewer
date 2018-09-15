package com.mj.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;

import com.mj.brewer.model.Cidade;
import com.mysql.jdbc.StringUtils;

public class CidadeConverter implements Converter<String, Cidade> {

	@Override
	public Cidade convert(String id) {
		return !StringUtils.isNullOrEmpty(id) ? new Cidade(Long.valueOf(id)) : null;
	}

}
