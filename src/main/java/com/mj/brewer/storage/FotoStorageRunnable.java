package com.mj.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.mj.brewer.dto.FotoDTO;

public class FotoStorageRunnable implements Runnable {

	private IFotoStorage fotoStorage;
	private DeferredResult<FotoDTO> result;
	private MultipartFile file;

	public FotoStorageRunnable(MultipartFile file, DeferredResult<FotoDTO> result, IFotoStorage fotoStorage) {
		super();
		this.fotoStorage = fotoStorage;
		this.result = result;
		this.file = file;
	}

	@Override
	public void run() {
		String novoNome = fotoStorage.salvarTemporariamente(file);
		result.setResult(new FotoDTO(novoNome, file.getOriginalFilename(), file.getContentType()));
	}

}
