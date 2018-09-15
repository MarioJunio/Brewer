var Brewer = Brewer || {};

Brewer.MaskMoney = (function() {

	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}

	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({
			thousands : '.',
			decimal : ','
		});

		this.plain.maskMoney({
			precision : 0,
			thousands : '.'
		});
	}

	return MaskMoney;

})();

Brewer.Mask = (function() {

	function Mask() {
		this.phone = $('.js-phone');
		this.docCpfCnpj = $('.js-doc-cpf-cnpj');
		this.lbCPFCNPJ = $('[for=cpfcnpj]');
		this.cpfcnpj = $('#cpfcnpj');
	}

	Mask.prototype.enable = function() {

		// verifica se já existe algum tipo de documento selecionado
		var docChecked = this.docCpfCnpj.filter(':checked')[0];

		if (docChecked) {
			documentoChange.call(this, docChecked, true);
			this.cpfcnpj.removeAttr('disabled');
		} else
			this.cpfcnpj.attr('disabled');

		var maskBehavior = function(val) {
			return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000'
					: '(00) 0000-00009';
		}

		var options = {
			onKeyPress : function(val, e, field, options) {
				field.mask(maskBehavior.apply({}, arguments), options);
			}
		};

		this.phone.mask(maskBehavior, options);

		this.docCpfCnpj.on('change', onDocumentoChange.bind(this));
	}

	function onDocumentoChange(event) {
		documentoChange.call(this, $(event.currentTarget), false);
	}

	function documentoChange(radio, edit) {

		// se não for edição ou reload
		if (!edit)
			this.cpfcnpj.val('');

		this.cpfcnpj.removeAttr('disabled');

		var selectedRadio = $(radio);
		var documento = selectedRadio.data("documento");
		var mascara = selectedRadio.data("mascara");

		this.lbCPFCNPJ.text(documento);
		this.cpfcnpj.mask(mascara, {
			reverse : true
		});

		this.cpfcnpj.focus();
	}

	return Mask;

})();

Brewer.MaskDate = (function() {

	function MaskDate() {
		this.inputDate = $('.js-date');
	}

	MaskDate.prototype.enable = function() {

		// inicializar o datepicker para campos de data
		this.inputDate.datepicker({
			format : 'dd/mm/yyyy',
			language : 'pt-BR',
			autoclose : true,
			orientation : 'bottom'
		});

		// mascara de data
		this.inputDate.mask('00/00/0000');
	}

	return MaskDate;
})();

Brewer.Security = (function() {

	function Security() {
		this.headerName = $('input[name=_csrf_header]').val();
		this.token = $('input[name=_csrf]').val();
	}

	Security.prototype.enable = function() {

		$(document).ajaxSend(function(event, jqxhr, settings) {
			jqxhr.setRequestHeader(this.headerName, this.token);
		}.bind(this));
	}

	return Security;

})();

numeral.locale('pt-br');

Brewer.formatarMoeda = function(valor) {
	return numeral(valor).format('0,0.00');
}

Brewer.removerFormatoMoeda = function(valor) {
	return numeral().unformat(valor);
}

$(function() {
	var mask = new Brewer.Mask();
	var maskMoney = new Brewer.MaskMoney();
	var maskDate = new Brewer.MaskDate();
	var security = new Brewer.Security();

	mask.enable();
	maskMoney.enable();
	maskDate.enable();
	security.enable();

});