package com.mj.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	String PREFIX_DOT_THUMBNAIL = "thumbnail.";

	public String salvar(MultipartFile[] files);
	
	public byte[] recuperar(String foto);
	
	public byte[] recuperarThumbnail(String foto);
	
	public void excluir(String foto);
	
	public String url(String foto);
	
	public String urlThumb(String foto);
	
}
