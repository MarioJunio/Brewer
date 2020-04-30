package com.mj.brewer.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mj.brewer.dto.VendaMes;
import com.mj.brewer.dto.VendaOrigem;
import com.mj.brewer.service.CervejaService;
import com.mj.brewer.service.ClienteService;
import com.mj.brewer.service.VendaService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	private static final String DASHBOARD_PAGE = "dashboard";

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private CervejaService cervejaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView(DASHBOARD_PAGE);
		mv.addObject("vendasAno", vendaService.vendasNoAno(Year.now().getValue()));
		mv.addObject("vendasMes", vendaService.vendasNoMes(LocalDate.now().getMonth().getValue()));
		mv.addObject("ticketMedio", vendaService.ticketMedio(Year.now().getValue()));
		mv.addObject("valorEstoque", cervejaService.valorTotalEstoque());
		mv.addObject("itensEstoque", cervejaService.itensNoEstoque());
		mv.addObject("quantidadeClientes", clienteService.quantidadeClientes());
		
		return mv;
	}
	
	@GetMapping("/chart-vendas-meses")
	@ResponseBody
	public List<VendaMes> chartVendasMeses() {
		return vendaService.chartVendasMeses();
	}
	
	@GetMapping("/chart-vendas-origem")
	@ResponseBody
	public List<VendaOrigem> chartVendasOrigem() {
		return vendaService.chartVendasOrigem();
	}
}
