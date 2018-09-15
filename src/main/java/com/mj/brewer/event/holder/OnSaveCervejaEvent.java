package com.mj.brewer.event.holder;

import com.mj.brewer.model.Cerveja;
import com.mysql.jdbc.StringUtils;

public class OnSaveCervejaEvent {

	private Cerveja cerveja;

	public OnSaveCervejaEvent() {
		super();
	}

	public OnSaveCervejaEvent(Cerveja cerveja) {
		super();
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}

	public void setCerveja(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public boolean temFoto() {
		return cerveja != null ? !StringUtils.isNullOrEmpty(cerveja.getFoto()) : false;
	}

	public boolean naoTemFoto() {
		return StringUtils.isNullOrEmpty(cerveja.getFoto());
	}
}
