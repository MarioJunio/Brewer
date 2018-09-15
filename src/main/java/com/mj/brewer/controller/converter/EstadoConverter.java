package com.mj.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;

import com.mj.brewer.model.Estado;
import com.mysql.jdbc.StringUtils;

public class EstadoConverter implements Converter<String, Estado> {

	@Override
	public Estado convert(String id) {
		return !StringUtils.isNullOrEmpty(id) ? new Estado(Long.valueOf(id)) : null;
	}

}
