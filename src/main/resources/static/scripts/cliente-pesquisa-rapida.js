var Brewer = Brewer || {};

Brewer.ClientePesquisaRapida = (function() {

	function ClientePesquisaRapida() {
		this.modal = $('#pesquisaRapidaClientes');
		this.form = $('.js-form-pesquisa-rapida-cliente');
		this.inputNome = $('.js-input-nome');
		this.btnPesquisar = $('.js-btn-pesquisa-rapida-cliente');
		this.alertErroValidacao = $('.js-alert-clientes-pesquisa-rapida');
		this.mensagemErroValidacao = $('.js-mensagem-erro-clientes-pesquisa-rapida');
		this.template = $('#hbs-clientes-pesquisa-rapida').html();
		this.hbsContent = $('#hbs-clientes-pesquisa-rapida-content');
	}

	ClientePesquisaRapida.prototype.iniciar = function() {
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		this.form.submit(osSubmitPesquisa.bind(this));
	}
	
	function onModalShow() {
		this.inputNome.focus();
	}

	function osSubmitPesquisa(e) {
		e.preventDefault();

		var uri = this.form.attr('action');
		var method = this.form.attr('method');
		var nome = this.inputNome.val();

		$.ajax({
			url : uri,
			type : method,
			contentType : 'application/json',
			data : {
				nome : nome
			},
			success : onPesquisaSuccess.bind(this),
			error : onPesquisaError.bind(this)
		});
	}

	function onPesquisaSuccess(data) {
		this.alertErroValidacao.addClass('hidden');
		
		var compiledTemplate = Handlebars.compile(this.template);
		var html = compiledTemplate({
			clientes: data
		});
		
		this.hbsContent.html(html);
		
		var clienteSelecionado = new Brewer.ClienteSelecionado(this.modal);
		clienteSelecionado.iniciar();
	}

	function onPesquisaError(error) {
		this.mensagemErroValidacao.text(error.responseText);
		this.alertErroValidacao.removeClass('hidden');
	}

	return ClientePesquisaRapida;
})();

Brewer.ClienteSelecionado = (function () {
	
	function ClienteSelecionado(modal) {
		this.modal = modal;
		this.rowCliente = $('.js-row-clientes-pesquisa-rapida');
		this.nomeClienteVenda = $('#nomeCliente');
		this.codigoClienteVenda = $('#codigoCliente');
	}
	
	ClienteSelecionado.prototype.iniciar = function() {
		this.rowCliente.on('click', onClienteSelecionado.bind(this));
	}
	
	function onClienteSelecionado(e) {
		var row = $(e.currentTarget);
		
		var codigo = row.data('codigo');
		var nome = row.data('nome');
		
		this.codigoClienteVenda.val(codigo);
		this.nomeClienteVenda.val(nome);
		
		this.modal.modal('hide');
	}
	
	return ClienteSelecionado;
})();


$(function() {

	var clientePesquisaRapida = new Brewer.ClientePesquisaRapida();
	clientePesquisaRapida.iniciar();

});