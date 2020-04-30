package com.mj.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;

public class FotoDeleteRunnable implements Runnable {

	private FotoStorage fotoStorage;
	private String nomeFoto;
	private DeferredResult<String> result;

	public FotoDeleteRunnable(FotoStorage fotoStorage, String nomeFoto, DeferredResult<String> result) {
		super();
		this.fotoStorage = fotoStorage;
		this.nomeFoto = nomeFoto;
		this.result = result;
	}

	@Override
	public void run() {
		fotoStorage.excluir(nomeFoto);
		fotoStorage.excluir(FotoStorage.PREFIX_DOT_THUMBNAIL + nomeFoto);
		result.setResult("ok");
	}

}
