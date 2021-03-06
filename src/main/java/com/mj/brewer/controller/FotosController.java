package com.mj.brewer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.mj.brewer.dto.FotoDTO;
import com.mj.brewer.storage.FotoDeleteRunnable;
import com.mj.brewer.storage.FotoStorage;
import com.mj.brewer.storage.FotoStorageRunnable;

@RestController
@RequestMapping("/fotos")
public class FotosController {

	@Autowired
	private FotoStorage fotoStorage;

	@PostMapping("/cerveja")
	public DeferredResult<FotoDTO> fotosCerveja(@RequestParam("files[]") MultipartFile file) {
		DeferredResult<FotoDTO> result = new DeferredResult<>();
		new Thread(new FotoStorageRunnable(file, result, fotoStorage)).start();
		return result;
	}

	@GetMapping("/cerveja/{nome:.*}")
	public byte[] recuperar(@PathVariable String nome) throws IOException {
		return fotoStorage.recuperar(nome);
	}
	
	@GetMapping("/cerveja/tmp/{nome:.*}")
	public byte[] recuperarTemp(@PathVariable String nome) {
		return fotoStorage.recuperar(nome);
	}

	@PostMapping("/cerveja/excluir/{nome:.*}")
	public DeferredResult<String> excluir(@PathVariable String nome) {
		DeferredResult<String> result = new DeferredResult<String>();
		new Thread(new FotoDeleteRunnable(fotoStorage, nome, result)).start();
		return result;

	}

	@PostMapping("/cerveja/excluir/tmp/{nome:.*}")
	public DeferredResult<String> excluirTemp(@PathVariable String nome) {
		DeferredResult<String> result = new DeferredResult<String>();
		new Thread(new FotoDeleteRunnable(fotoStorage, "temp/" + nome, result)).start();
		return result;

	}

}
