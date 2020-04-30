package com.mj.brewer.event.holder;

import com.mj.brewer.model.Venda;

public class OnVendaSaveEvent {

	private Venda venda;

	public OnVendaSaveEvent() {
		super();
	}

	public OnVendaSaveEvent(Venda venda) {
		super();
		this.venda = venda;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
