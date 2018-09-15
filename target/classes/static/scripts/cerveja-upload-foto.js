var Brewer = Brewer || {};

Brewer.CervejaUploadFoto = (function() {

	function CervejaUploadFoto() {

		this.fieldNome = $('input[name=foto]');
		this.fieldContentType = $('input[name=contentType]');

		this.containerFoto = $('.js-foto-cerveja-container');
		this.fotoUpload = $('.js-foto-cerveja-upload');
		this.fotoCervejaTemplate = $('#upload-foto-cerveja');

		this.fotoCervejaHtml = null;
		this.btnDelFotoCervejaUpload = null;
		this.nova = false;

		// compilando html com handlebars
		this.template = Handlebars.compile(this.fotoCervejaTemplate.html());
	}

	CervejaUploadFoto.prototype.iniciar = function() {

		this.settings = {
			type : 'json',
			action : $('.js-upload-fotos-controller').text(),
			allow : '*.(jpg|jpeg|gif|png)',
			filelimit : 1,
			complete : onCompleteUpload.bind(this),
			beforeSend : adicionarCSRF
		}

		UIkit.uploadSelect($('#upload-select'), this.settings);
		UIkit.uploadDrop($('#upload-drop'), this.settings);

		if (this.fieldNome.val() && this.fieldContentType.val()) {

			onCompleteUpload.call(this, {
				nome : this.fieldNome.val(),
				nomeOriginal : getNomeOriginal(this.fieldNome.val()),
				contentType : this.fieldContentType.val()
			});
			
		} else
			this.nova = true;

	}

	function adicionarCSRF(xhr) {
		var headerName = $('input[name=_csrf_header]').val();
		var token = $('input[name=_csrf]').val();

		xhr.setRequestHeader(headerName, token);
	}

	function onCompleteUpload(response) {

		// atribui os dados da resposta aos campos ocultos
		this.fieldNome.val(response.nome);
		this.fieldContentType.val(response.contentType);

		var cervejaId = $('.js-cerveja-id').val();
		var foto = null;

		// se existir o ID da cerveja então é uma edição, se não é cadastro
		if (cervejaId && !this.nova)
			foto = response.nome;
		else
			foto = 'tmp/' + response.nome;

		var htmlTemplate = this.template({
			nome : foto,
			nomeOriginal : response.nomeOriginal
		});

		// esconde o campo para fazer upload da foto
		this.fotoUpload.addClass('hidden');

		// adiciona foto da cerveja para o usuário visualizar o thumbnail
		this.containerFoto.append(htmlTemplate);

		// após adicionado o HTML compilado podemos buscar os elementos
		this.fotoCervejaHtml = $('.js-foto-cerveja');
		this.btnDelFotoCervejaUpload = $('.js-btn-del-foto-cerveja-upload');

		// exibe a foto da uplaod carregada
		this.fotoCervejaHtml.removeClass('hidden');

		// excluir foto cerveja carregada
		this.btnDelFotoCervejaUpload.on('click', onDeleteUpload.bind(this));

	}

	function onDeleteUpload() {

		$.ajax({
			url : this.btnDelFotoCervejaUpload.attr('href'),
			method : 'POST',
			success : onDeleteUploadSuccess.bind(this),
			error : onDeleteUploadError.bind(this)
		})

		return false;

	}

	function onDeleteUploadSuccess(response) {

		if (response == 'ok') {
			this.nova = true;

			this.fieldNome.val('');
			this.fieldContentType.val('');

			this.fotoCervejaHtml.remove();
			this.fotoUpload.removeClass('hidden');
		}
	}

	function onDeleteUploadError(error) {
		console.log(error);
	}

	function getNomeOriginal(nome) {
		var index = nome.indexOf('_');

		if (index >= 0)
			return nome.substring(index + 1, nome.length);

		return null;
	}

	return CervejaUploadFoto;

})();

$(function() {
	console.log($('.js-upload-fotos-controller').text());
	var cervejaUploadFoto = new Brewer.CervejaUploadFoto();
	cervejaUploadFoto.iniciar();
});