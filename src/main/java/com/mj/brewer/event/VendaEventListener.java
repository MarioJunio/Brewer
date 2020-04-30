package com.mj.brewer.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mj.brewer.event.holder.OnVendaSaveEvent;
import com.mj.brewer.model.ItemVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.service.CervejaService;

@Component
public class VendaEventListener {

	@Autowired
	private CervejaService cervejaService;
	
	@EventListener
	public void onVendaSaved(OnVendaSaveEvent onVendaSaved) {
		Venda venda = onVendaSaved.getVenda();
		
		// itera sobre os itens da venda, subtraindo os estoques das cervejas vendidas
		for (ItemVenda iv : venda.getItens()) {
			cervejaService.subtrairEstoque(iv.getCerveja(), iv.getQuantidade().intValue());
		}
		
	}
	
}
