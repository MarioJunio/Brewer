package com.mj.brewer.dto;

public class FotoDTO {

	private String nome;
	private String nomeOriginal;
	private String contentType;
	private String url;

	public FotoDTO(String nome, String nomeOriginal, String contentType, String url) {
		super();
		this.nome = nome;
		this.nomeOriginal = nomeOriginal;
		this.contentType = contentType;
		this.url = url;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
