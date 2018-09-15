var Brewer = Brewer || {};

Brewer.EstadoCidadeSelect = (function() {

	function EstadoCidadeSelect() {
		this.estadoSelect = $('.js-select-estados');
		this.cidadeSelect = $('.js-select-cidades');
		this.cidadeSelected = $('.js-selected-cidade');
	}

	EstadoCidadeSelect.prototype.enable = function() {

		this.estadoSelect.on('change',
				function(e) {
					var estadoId = $(e.currentTarget).find(':selected').val();
					var cidadeEstadoUrl = $(e.currentTarget).data(
							'cidades-estado-url');
					buscarCidades.call(this, cidadeEstadoUrl
							+ estadoId.toString());
				}.bind(this));

		// carrega as cidades do estado selecionado se houver
		carregarCidadesEstado.call(this);
	}

	function buscarCidades(cidadeEstadoUrl) {

		$.ajax({
			url : cidadeEstadoUrl,
			method : 'GET',
			beforeSend : function() {
				limparCidades.call(this);
			}.bind(this),
			success : function(data) {
				var cidades = [];

				data.forEach(function(item, index) {
					cidades.push('<option id="cb_cidade_' + item.id
							+ '" value="' + item.id + '">' + item.nome
							+ '</option>');
				}.bind(this));

				this.cidadeSelect.html(cidades.join(''));
				this.cidadeSelect.removeAttr('disabled');

				cidadeSelecionada.call(this);

			}.bind(this),

			error : function(data) {
				console.log('Falha: ' + data);
			}.bind(this)
		});

		function limparCidades() {
			this.cidadeSelect.html('');
			this.cidadeSelect.append('<option value="">Selecione</option>');
			this.cidadeSelect.attr('disabled', true);
		}
	}

	/**
	 * Carrega as cidades do estado selecionado
	 */
	function carregarCidadesEstado() {
		var estadoSelected = $('.js-select-estados').find(':selected');

		if (estadoSelected.val()) {
			var idEstado = estadoSelected.val();
			var cidadeEstadoUrl = estadoSelected.parent().data('cidades-estado-url');
			
			buscarCidades.call(this, cidadeEstadoUrl + idEstado.toString());
		}
	}

	/**
	 * Manter a cidade atual selecionada se houver, caso a página seja
	 * atualizada
	 */
	function cidadeSelecionada() {
		var idCidadeSelected = this.cidadeSelected.val();

		// se houver alguma cidade selecionada
		if (idCidadeSelected) {
			var cbCidade = $('#cb_cidade_' + idCidadeSelected);

			cbCidade.attr('selected', true);
			cbCidade.removeAttr('disabled');
		}
	}

	return EstadoCidadeSelect;

})();

$(function() {
	var estadoCidadeSelect = new Brewer.EstadoCidadeSelect();
	estadoCidadeSelect.enable();
});