package com.mj.brewer.event.holder;

import com.mj.brewer.model.Cerveja;

public class OnDeleteCervejaEvent {

	private Cerveja cerveja;

	public OnDeleteCervejaEvent(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}

	public boolean temFoto() {

		if (cerveja != null)
			return cerveja.temFoto();

		return false;
	}

}
