package com.mj.brewer.storage;

import java.io.IOException;

import org.springframework.web.context.request.async.DeferredResult;

public class FotoDeleteRunnable implements Runnable {

	private IFotoStorage fotoStorage;
	private String nomeFoto;
	private DeferredResult<String> result;
	private boolean temp;

	public FotoDeleteRunnable(IFotoStorage fotoStorage, String nomeFoto, DeferredResult<String> result, boolean temp) {
		super();
		this.fotoStorage = fotoStorage;
		this.nomeFoto = nomeFoto;
		this.result = result;
		this.temp = temp;
	}

	@Override
	public void run() {
		try {

			// se for uma edição, então é necessário excluir o thumb e a original
			if (!temp)
				result.setResult(fotoStorage.excluir(nomeFoto) && fotoStorage.excluirThumb(nomeFoto) ? "ok" : "");
			else
				result.setResult(fotoStorage.excluir(nomeFoto) ? "ok" : "");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
