package com.mj.brewer.event;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mj.brewer.event.holder.OnDeleteCervejaEvent;
import com.mj.brewer.event.holder.OnSaveCervejaEvent;
import com.mj.brewer.storage.IFotoStorage;

@Component
public class CervejaEventListener {

	@Autowired
	private IFotoStorage fotoStorage;

	@EventListener(condition = "#event.temFoto()")
	public void onSaveCerveja(OnSaveCervejaEvent event) {
		fotoStorage.salvar(event.getCerveja().getFoto());
		fotoStorage.criarThumb(event.getCerveja().getFoto());
	}

	@EventListener(condition = "#event.temFoto()")
	public void onDeleteCerveja(OnDeleteCervejaEvent event) throws IOException {
		fotoStorage.excluir(event.getCerveja().getFoto());
		fotoStorage.excluirThumb(event.getCerveja().getFoto());
	}

}
