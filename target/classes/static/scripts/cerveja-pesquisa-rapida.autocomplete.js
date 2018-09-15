var Brewer = Brewer || {};

Brewer.CervejaAutocomplete = (function() {

	function CervejaAutocomplete() {
		this.inputCerveja = $('.js-cerveja-pesquisa-rapida');
		this.templateHTML = $('#hbs-cervejas-pesquisa-rapida');
		this.template = Handlebars.compile(this.templateHTML.html());
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	CervejaAutocomplete.prototype.iniciar = function() {

		var options = {
			url : function(nomeOuSKU) {
				return this.inputCerveja.data('uri') + '=' + nomeOuSKU;
			}.bind(this),

			getValue : 'nome',

			requestDelay : 300,

			minCharNumber : 3,

			ajaxSettings : {
				contentType : 'application/json'
			},
			template : {
				type : 'custom',
				method : itemTemplate.bind(this)
			},
			list : {
				onChooseEvent : onItemSelecionado.bind(this)
			}

		};

		this.inputCerveja.easyAutocomplete(options);
	}

	function itemTemplate(nome, cerveja) {
		cerveja.valorFormatado = Brewer.formatarMoeda(cerveja.valor);
		return this.template(cerveja);
	}

	function onItemSelecionado() {
		var item = this.inputCerveja.getSelectedItemData();
		this.emitter.trigger('item-selecionado', item);
	}
	
	return CervejaAutocomplete;
})();