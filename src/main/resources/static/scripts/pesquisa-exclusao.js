var Brewer = Brewer || {};

Brewer.PesquisaExclusao = (function() {

	function PesquisaExclusao() {
		this.btnExclusao = $('.js-btn-excluir');
	}

	PesquisaExclusao.prototype.iniciar = function() {
		this.btnExclusao.on('click', onExcluirObjeto.bind(this));
	}

	function onExcluirObjeto(e) {
		e.preventDefault();
		
		var btnClicked = $(e.currentTarget);
		var deleteMessage = btnClicked.data('delete-message');
		var url = btnClicked.data('url');
		var objeto = btnClicked.data('objeto');
		
		swal({
			  title: 'Você tem certeza?',
			  text: deleteMessage,
			  type: 'question',
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Sim, eu tenho!!'
			}).then((result) => {

				// se confirmou
				if (result.value) {
					
					$.ajax({
						url: url,
						method: 'DELETE',
						success: onObjetoExcluido,
						error: onErroExclusao
					});
				}
				
			});
	}
	
	function onObjetoExcluido(message) {
		swal('Excluído', message, 'success').then(() => location.reload());
	}
	
	function onErroExclusao(error) {
		swal('Ops', error.responseText, 'error');
	}

	return PesquisaExclusao;

})();

$(function() {

	var pesquisaExclusao = new Brewer.PesquisaExclusao();
	pesquisaExclusao.iniciar();

});