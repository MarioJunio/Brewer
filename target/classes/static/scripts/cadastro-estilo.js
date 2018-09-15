var Brewer = Brewer || {};

Brewer.EstiloCadastroRapido = (function() {

	function EstiloCadastroRapido() {
		this.modal = $('.bs-cadastro-estilo');
		this.formCadEstilo = $('.js-form-cadastro-estilo');
		this.inputNome = this.formCadEstilo.find('#nome');
		this.btnSalvar = this.formCadEstilo.find('.js-btn-salvar');
		this.errorPanel = $('.js-error-message');
		this.groupFieldNome = $('.js-form-field-nome');

	}

	EstiloCadastroRapido.prototype.iniciar = function() {
		this.modal.on('show.bs.modal', modalCadEstiloShow.bind(this));
		this.modal.on('hidden.bs.modal', modalCadEstiloClose.bind(this));

		this.formCadEstilo.on('submit', function(e) {
			e.preventDefault();
		});

		this.btnSalvar.on('click', function() {

			var action = this.formCadEstilo.attr('action');
			var formMethod = this.formCadEstilo.attr('method');
			var fNome = this.inputNome.val().trim();

			$.ajax({
				url : action,
				method : formMethod.toUpperCase().trim(),
				contentType : 'application/json',
				data : JSON.stringify({
					nome : fNome
				}),

				success : function(data, text) {
					// adiciona novo estilo na seleção
					$('.js-cb-estilo').append(
							'<option value="' + data.id + '" selected>'
									+ data.nome + '</option>');

					// fecha o modal de cadastro
					this.modal.modal('hide');

				}.bind(this),

				error : function(r, s, e) {
					this.errorPanel.find('.js-error-message').text(
							r.responseText);
					this.errorPanel.removeClass('hide');
					this.groupFieldNome.addClass('has-error');
				}.bind(this)
			});

		}.bind(this));
	}

	function modalCadEstiloShow() {
		this.inputNome.focus();
	}

	function modalCadEstiloClose() {
		this.errorPanel.addClass('hide');
		this.groupFieldNome.removeClass('has-error');
		this.inputNome.val('');
	}

	return EstiloCadastroRapido;

})();

$(function() {

	var estiloCadastroRapido = new Brewer.EstiloCadastroRapido();
	estiloCadastroRapido.iniciar();

});