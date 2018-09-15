package com.mj.brewer.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFotoStorage {

	public void salvar(String nome);
	
	public void criarThumb(String nome);

	public byte[] recuperar(String nome) throws IOException;

	public String salvarTemporariamente(MultipartFile multipartFile);

	public byte[] recuperarTemporario(String nome);

	byte[] recuperarThumbnail(String nome) throws IOException;

	public boolean excluir(String foto) throws IOException;

	public boolean excluirThumb(String foto) throws IOException;
}
