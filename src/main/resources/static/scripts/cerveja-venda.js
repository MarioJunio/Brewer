var Brewer = Brewer || {};

Brewer.VendaCerveja = (function() {

	function VendaCerveja(cervejaSelecionadaVenda) {
		this.cervejaSelecionadaVenda = cervejaSelecionadaVenda;
		this.lbTotalVenda = $('.js-total-venda');
		this.inpFrete = $('.js-venda-frete');
		this.inpDesconto = $('.js-venda-desconto');
	}

	VendaCerveja.prototype.iniciar = function() {
		this.cervejaSelecionadaVenda.on('venda-atualizada', recalcularTotalVenda.bind(this));

		this.inpFrete.on('keyup', recalcularTotalVenda.bind(this));
		this.inpDesconto.on('keyup', recalcularTotalVenda.bind(this));

		recalcularTotalVenda.call(this);
	}

	function recalcularTotalVenda(event) {
		var total = numeral($('.js-venda-total').val()).value();
		var frete = numeral(this.inpFrete.val()).value();
		var desconto = numeral(this.inpDesconto.val()).value();

		console.log('total', total);
		console.log('frete', frete);
		console.log('desconto', desconto);

		total = !total ? 0 : total;
		frete = !frete ? 0 : frete;
		desconto = !desconto ? 0 : desconto;

		total += frete - desconto;

		this.lbTotalVenda.removeClass('success-title');
		this.lbTotalVenda.removeClass('danger-title');
		this.lbTotalVenda.removeClass('default-title');

		if (total > 0)
			this.lbTotalVenda.addClass('success-title');
		else if (total < 0)
			this.lbTotalVenda.addClass('danger-title');
		else
			this.lbTotalVenda.addClass('default-title');

		this.lbTotalVenda.text('R$ ' + Brewer.formatarMoeda(total));

	}

	return VendaCerveja;

})();

$(function() {

	var cervejaAutocomplete = new Brewer.CervejaAutocomplete();
	var cervejaSelecionadaVenda = new Brewer.CervejaSelecionadaVenda(cervejaAutocomplete);
	var vendaCerveja = new Brewer.VendaCerveja(cervejaSelecionadaVenda);

	cervejaAutocomplete.iniciar();
	cervejaSelecionadaVenda.iniciar();
	vendaCerveja.iniciar();

});