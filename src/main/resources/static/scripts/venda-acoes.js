var Brewer = Brewer || {};

Brewer.VendaAcoes = (function() {

	function VendaAcoes() {
		this.formVenda = $('.js-formulario-venda');
		this.btnsSalvarVenda = $('.js-btn-salvar');
		this.btnSalvarAcao = $('.js-input-acao');
	}

	VendaAcoes.prototype.iniciar = function() {
		this.btnsSalvarVenda.on('click', onSalvarVenda.bind(this));
	}

	function onSalvarVenda(e) {
		e.preventDefault();

		var btnClicado = $(e.currentTarget);
		this.btnSalvarAcao.attr('name', btnClicado.data('acao'));

		this.formVenda.submit();
	}

	return VendaAcoes;

})();

$(function() {
	var vendaAcoes = new Brewer.VendaAcoes();
	vendaAcoes.iniciar();
});