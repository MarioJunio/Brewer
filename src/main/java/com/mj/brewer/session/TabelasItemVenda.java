package com.mj.brewer.session;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.ItemVenda;

@Component
@SessionScope
public class TabelasItemVenda {

	private Map<String, TabelaItemVenda> tabelas = new HashMap<>();

	public void adicionarItem(String uuid, Cerveja cerveja, BigDecimal one) {
		TabelaItemVenda tabela = getOrCreateTabelaItemVenda(uuid);
		tabela.addItemVenda(cerveja, one);
	}

	private TabelaItemVenda getOrCreateTabelaItemVenda(String uuid) {
		TabelaItemVenda tabelaItens = tabelas.get(uuid);

		if (tabelaItens == null) {
			tabelaItens = new TabelaItemVenda();
			tabelas.put(uuid, tabelaItens);
		}

		return tabelaItens;
	}

	public void atualizarQuantidadeItem(String uuid, Cerveja cerveja, BigDecimal quantidade) {
		TabelaItemVenda tabela = getOrCreateTabelaItemVenda(uuid);
		tabela.atualizarQuantidade(cerveja, quantidade);
	}

	public void excluirItem(String uuid, Cerveja cerveja) {
		TabelaItemVenda tabela = getOrCreateTabelaItemVenda(uuid);
		tabela.deletarItemVenda(cerveja);
	}

	public List<ItemVenda> getItens(String uuid) {
		TabelaItemVenda tabela = getOrCreateTabelaItemVenda(uuid);
		return tabela.getItensVenda();
	}

	public BigDecimal getValorTotal(String uuid) {
		TabelaItemVenda tabela = getOrCreateTabelaItemVenda(uuid);
		return tabela.getValorTotal();
	}

}
