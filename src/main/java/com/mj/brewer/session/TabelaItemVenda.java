package com.mj.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.ItemVenda;

class TabelaItemVenda {

	private List<ItemVenda> itensVenda = new ArrayList<>();

	public void addItemVenda(Cerveja cerveja, BigDecimal valorUnitario, BigDecimal quantidade) {
		Optional<ItemVenda> opItem = getCerveja(cerveja);

		if (opItem.isPresent()) {
			ItemVenda it = opItem.get();
			it.setQuantidade(it.getQuantidade().add(quantidade));
		} else {
			ItemVenda itemVenda = new ItemVenda();
			itemVenda.setCerveja(cerveja);
			itemVenda.setValorUnitario(valorUnitario);
			itemVenda.setQuantidade(quantidade);

			itensVenda.add(0, itemVenda);
		}

	}

	public void addItemVenda(Cerveja cerveja, BigDecimal quantidade) {
		this.addItemVenda(cerveja, cerveja.getValor(), quantidade);
	}

	public void atualizarQuantidade(Cerveja cerveja, BigDecimal quantidade) {
		Optional<ItemVenda> opItemVenda = getCerveja(cerveja);

		if (opItemVenda.isPresent()) {
			ItemVenda iv = opItemVenda.get();
			iv.setQuantidade(quantidade);
		}
	}

	public void deletarItemVenda(Cerveja cerveja) {
		OptionalInt index = IntStream.range(0, itensVenda.size()).filter(i -> itensVenda.get(i).getCerveja().equals(cerveja)).findFirst();

		if (index.isPresent())
			itensVenda.remove(index.getAsInt());
	}

	public Optional<ItemVenda> getCerveja(Cerveja cerveja) {
		return itensVenda.stream().filter(c -> c.getCerveja().equals(cerveja)).findFirst();
	}

	public Optional<ItemVenda> getCerveja(Long id) {
		return itensVenda.stream().filter(c -> c.getCerveja().getId().equals(id)).findFirst();
	}

	public BigDecimal getValorTotal() {
		return itensVenda.stream().map(ItemVenda::getValorTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

}
