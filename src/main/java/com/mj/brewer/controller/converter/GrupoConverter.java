package com.mj.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;

import com.mj.brewer.model.Grupo;
import com.mysql.jdbc.StringUtils;

public class GrupoConverter implements Converter<String, Grupo> {

	@Override
	public Grupo convert(String codigo) {

		if (!StringUtils.isNullOrEmpty(codigo))
			return new Grupo(Long.parseLong(codigo));

		return null;
	}

}
