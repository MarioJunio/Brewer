package com.mj.brewer.service.exception;

public class ImpossivelExcluirEntidade extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ImpossivelExcluirEntidade(String message) {
		super(message);
	}

}
