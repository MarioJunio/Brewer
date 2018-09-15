var Brewer = Brewer || {};

Brewer.CervejaSelecionadaVenda = (function() {

	function CervejaSelecionadaVenda(cervejaAutocomplete) {
		this.cervejaAutocomplete = cervejaAutocomplete;
		this.tabelaItensVenda = $('.js-tabela-itens-venda');
		this.uuid = $('.js-input-uuid').val();

		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	CervejaSelecionadaVenda.prototype.iniciar = function() {
		this.cervejaAutocomplete.on('item-selecionado', adicionarAoCarrinho.bind(this));
		onAtualizarItemVenda.call(this);
	}

	function onAtualizarItemVenda() {
		$('.js-venda-item-quantidade').on('change', onItemVendaAtualizado.bind(this));
		$('.js-item-venda').on('dblclick', onExibirExclusaoItemVendaPanel.bind(this));
		$('.js-btn-excluir-item-venda').on('click', onExcluirItemVenda.bind(this));

		// mascara para formatar corretamente a quantidade de itens
		$('.js-venda-item-quantidade').maskMoney({
			precision : 0,
			thousands : ''
		});
		
		// emite um evento informando que a algum item da venda foi atualizado
		this.emitter.trigger('venda-atualizada');
	}

	function adicionarAoCarrinho(event, item) {
		$.ajax({
			url : "item",
			method : 'POST',
			data : {
				cervejaID : item.id,
				uuid : this.uuid
			},
			success : onItemAdicionado.bind(this),
			error : onItemNaoAdicionado.bind(this)
		});
	}

	function onItemAdicionado(data) {
		this.tabelaItensVenda.html(data);
		onAtualizarItemVenda.call(this);

		this.cervejaAutocomplete.inputCerveja.val('');
		this.cervejaAutocomplete.inputCerveja.focus();
	}

	function onItemNaoAdicionado(error) {
		console.log(error);
	}

	function onItemVendaAtualizado(event) {
		var inp = $(event.currentTarget);

		var response = $.ajax({
			url : 'item/' + inp.data('cerveja_id'),
			method : 'PUT',
			data : {
				uuid : this.uuid,
				quantidade : inp.val()
			},
			success : onItemAdicionado.bind(this)
		});

	}

	function onExibirExclusaoItemVendaPanel(event) {
		var pnExclusao = $(event.currentTarget).find('.bw-tabela-item__painel-exclusao').first()
		pnExclusao.toggleClass('solicitando-exclusao');
	}

	function onExcluirItemVenda(event) {
		var btnExclusao = $(event.currentTarget);

		$.ajax({
			url : 'item/' + this.uuid + '/' + btnExclusao.data('cerveja-id'),
			method : 'DELETE',
			success : onItemAdicionado.bind(this)
		});
	}

	return CervejaSelecionadaVenda;

})();