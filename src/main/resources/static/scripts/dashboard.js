var Brewer = Brewer || {};

Brewer.DashboardChart = (function() {

	function DashboardChart() {
		this.canvasChartVendasMeses = $('#graficoVendasPorMes');
		this.canvasChartVendasOrigem = $('#graficoVendasPorOrigem');
	}

	DashboardChart.prototype.iniciar = function() {
		carregarChartVendasMeses.call(this);
		carregarChartVendasOrigem.call(this);
	}
	
	function carregarChartVendasMeses() {
		var url = this.canvasChartVendasMeses.data('url');
		
		$.ajax({
			url: url,
			method: 'GET',
			success: onSuccessChartVendasMeses.bind(this)
		});
	}
	
	function onSuccessChartVendasMeses(vendasMeses) {
		var noDataChart = this.canvasChartVendasMeses.parent().find('.js-no-data-chart');
		
		// se for retornado algum dado
		if (vendasMeses.length) {
			noDataChart.hide();
			var meses = [];
			var quantidades = [];
			
			// itera sobre a array de vendas por meses
			vendasMeses.forEach(vendaMes => {
				meses.unshift(vendaMes.mes);
				quantidades.unshift(vendaMes.quantidade);
			});
			
			// renderiza grafico na tela passando os dados de exibição
			construirGraficoVendasPorMeses.call(this, meses, quantidades);
			
		} else {
			noDataChart.show();
		}
	}
	
	function construirGraficoVendasPorMeses(meses, quantidades) {
		var ctx = this.canvasChartVendasMeses[0].getContext('2d');	
		
		new Chart(ctx, {
			type : 'line',
			data : {
				labels : meses,
				datasets: [{
					label: 'Vendas por Mês',
					backgroundColor: "rgba(26,179,148,0.5)",
					pointBorderColor: "rgba(26,179,148,1)",
					pointBackgroundColor: "#fff",
					data: quantidades
				}]
			}
		});
		
	}
	
	function carregarChartVendasOrigem() {
		var url = this.canvasChartVendasOrigem.data('url');
		
		$.ajax({
			url: url,
			method: 'GET',
			success: onSuccessChartVendasOrigem.bind(this)
		});
	}
	
	function onSuccessChartVendasOrigem(vendas) {
		var noDataChart = this.canvasChartVendasOrigem.parent().find('.js-no-data-chart');
		
		// se for retornado algum dado
		if (vendas.length) {
			noDataChart.hide();
			
			var meses = [];
			var quantidadeNacional = [];
			var quantidadeInternacional = [];
			
			// itera sobre a array de vendas por meses
			vendas.forEach(vendaOrigem => {
				meses.unshift(vendaOrigem.mes);
				quantidadeNacional.unshift(vendaOrigem.quantidadeNacional);
				quantidadeInternacional.unshift(vendaOrigem.quantidadeInternacional);
			});
			
			// renderiza grafico na tela passando os dados de exibição
			construirGraficoVendasPorOrigem.call(this, meses, quantidadeNacional, quantidadeInternacional);
			
		} else {
			noDataChart.show();
		}
	}
	
	function construirGraficoVendasPorOrigem(meses, quantidadeNacional, quantidadeInternacional) {
		var ctx = this.canvasChartVendasOrigem[0].getContext('2d');	
		
		new Chart(ctx, {
			type : 'bar',
			data : {
				labels : meses,
				datasets: [{	
					label: 'Nacional',
					backgroundColor: "rgba(164,200,225)",
					pointBorderColor: "rgba(26,179,148,1)",
					pointBackgroundColor: "#fff",
					data: quantidadeNacional
				},
				{
					label: 'Internacional',
					backgroundColor: "rgba(0,151,255)",
					pointBorderColor: "rgba(26,179,148,1)",
					pointBackgroundColor: "#fff",
					data: quantidadeInternacional
				}]
			}
		});
		
	}

	return DashboardChart;

})();

$(function() {
	var dashboard = new Brewer.DashboardChart();
	dashboard.iniciar();
});