package com.mj.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.mj.brewer.dto.FotoDTO;

public class FotoStorageRunnable implements Runnable {

	private FotoStorage fotoStorage;
	private DeferredResult<FotoDTO> result;
	private MultipartFile file;

	public FotoStorageRunnable(MultipartFile file, DeferredResult<FotoDTO> result, FotoStorage fotoStorage) {
		super();
		this.fotoStorage = fotoStorage;
		this.result = result;
		this.file = file;
	}

	@Override
	public void run() {
		String fotoRenomeada = fotoStorage.salvar(new MultipartFile[] { file });
		result.setResult(new FotoDTO(fotoRenomeada, file.getOriginalFilename(), file.getContentType(), fotoStorage.url(fotoRenomeada)));
	}

}
