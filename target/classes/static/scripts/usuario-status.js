var Brewer = Brewer || {};

Brewer.UsuarioStatus = (function() {

	function UsuarioStatus() {
		this.uri = $('.js-usuario-status-uri').data('uri');
		this.btnStatus = $('.js-btn-usuario-status');
		this.cbTodosSelecionados = $('.js-cb-usuario-marker-all');
		this.cbMarkers = $('.js-cb-usuario-marker');
	}

	UsuarioStatus.prototype.iniciar = function() {
		this.btnStatus.on('click', onAlterarStatus.bind(this));
		this.cbTodosSelecionados.on('change', onSelecionarTodos.bind(this));
		this.cbMarkers.on('change', onSelecionar.bind(this));
	}

	function onAlterarStatus(event) {
		var btnClicado = $(event.currentTarget);
		
		var cbSelecionados = this.cbMarkers.filter(':checked');
		var status = btnClicado.data('status');
		
		var codigosSelecionados = $.map(cbSelecionados, function (cb) {
			return $(cb).data('codigo');
		});
		
		if (codigosSelecionados.length > 0) {
			
			$.ajax({
				url: this.uri,
				method: 'PUT',
				data: {
					codigos: codigosSelecionados,
					status: status
				},
				success: function() {
					window.location.reload();
				}
			});
		}
		
	}
	
	function onSelecionarTodos(event) {
		var selecionou = $(event.currentTarget).is(":checked");
		this.cbMarkers.prop('checked', selecionou);
		this.btnStatus.prop('disabled', !selecionou);
	}

	function onSelecionar() {
		var countSelecionados = this.cbMarkers.filter(':checked');
		this.cbTodosSelecionados.prop('checked', countSelecionados.length >= this.cbMarkers.length);
		this.btnStatus.prop('disabled', !countSelecionados.length);
	}
	
	return UsuarioStatus;
})();

$(function() {

	var usuarioStatus = new Brewer.UsuarioStatus();
	usuarioStatus.iniciar();

});