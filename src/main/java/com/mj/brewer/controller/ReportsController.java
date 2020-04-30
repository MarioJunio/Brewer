package com.mj.brewer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mj.brewer.model.filter.ReportVendasEmitidasFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("/reports")
public class ReportsController {

	public static final String VIEW_CONSULTA = "relatorios/vendas-emitidas";

	@Autowired
	private DataSource dataSource;

	@GetMapping("/vendas-emitidas")
	public ModelAndView vendasEmitidas() {
		ModelAndView mv = new ModelAndView(VIEW_CONSULTA);
		mv.addObject("relatorioVendasEmitidasFilter", new ReportVendasEmitidasFilter());
		
		return mv;
	}

	@GetMapping("/vendas-emitidas/generate")
	@ResponseBody
	public void vendasEmitidasGenerate(ReportVendasEmitidasFilter reportVendasEmitidasFilter, HttpServletResponse response) {
		System.out.println(reportVendasEmitidasFilter.toString());

		try {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("format", "pdf");
			parametros.put("data_inicial", Date.from(reportVendasEmitidasFilter.getDataInicio().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametros.put("data_final", Date.from(reportVendasEmitidasFilter.getDataFim().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			InputStream reportInpStream = this.getClass().getResourceAsStream("/reports/vendas-emitidas.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportInpStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=vendas-emitidas.pdf");

			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
