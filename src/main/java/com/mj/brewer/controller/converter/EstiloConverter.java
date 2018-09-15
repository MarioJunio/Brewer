package com.mj.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;

import com.mj.brewer.model.Estilo;
import com.mysql.jdbc.StringUtils;

public class EstiloConverter implements Converter<String, Estilo> {

	@Override
	public Estilo convert(String id) {
		return !StringUtils.isNullOrEmpty(id) ? new Estilo(Long.valueOf(id)) : null;
	}

}
