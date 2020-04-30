package com.mj.brewer.model.listener;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.mj.brewer.model.Cerveja;
import com.mj.brewer.storage.FotoStorage;

public class CervejaListener {

	@Autowired
	private FotoStorage fotoStorage;

	@PostLoad
	public void postLoad(final Cerveja cerveja) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		cerveja.setUrl(fotoStorage.url(cerveja.temFoto() ? cerveja.getFoto() : "thumbnail.cerveja-mock.png"));
		cerveja.setUrlThumb(fotoStorage.urlThumb(cerveja.temFoto() ? cerveja.getFoto() : "cerveja-mock.png"));
	}

}
