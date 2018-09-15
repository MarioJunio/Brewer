package com.mj.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Cidade;
import com.mj.brewer.model.Estado;
import com.mj.brewer.model.filter.CidadeFilter;
import com.mj.brewer.repository.Cidades;
import com.mj.brewer.repository.Estados;
import com.mj.brewer.service.CidadeService;
import com.mj.brewer.service.exception.CidadeJaCadastradaException;

@Controller
@RequestMapping("/cidades")
public class CidadesController {

	public static final String VIEW_CADASTRO = "cidades/cadastro";
	public static final String VIEW_CONSULTA = "cidades/pesquisa";

	@Autowired
	private Cidades cidades;

	@Autowired
	private Estados estados;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping("/novo")
	public ModelAndView novo(Cidade cidade) {
		ModelAndView mv = new ModelAndView(VIEW_CADASTRO);
		mv.addObject("estados", estados.findAll());

		return mv;
	}

	@GetMapping
	public ModelAndView pesquisa(CidadeFilter cidadeFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(VIEW_CONSULTA);
		mv.addObject("estados", estados.findAll());
		mv.addObject("pagina", cidadeService.cidades(pageable, cidadeFilter, httpServletRequest));

		return mv;
	}

	@Cacheable(value = "cidades", key = "#estadoId")
	@GetMapping("/busca-estado/{estadoId}")
	public ResponseEntity<List<Cidade>> cidadesPorEstado(@PathVariable Long estadoId) {
		return ResponseEntity.ok().body(cidades.findByEstado(new Estado(estadoId)));
	}

	@CacheEvict(value = "cidades", key = "#cidade.estado.id", condition = "#cidade.temEstado()")
	@PostMapping
	public ModelAndView salvar(@Validated Cidade cidade, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

		if (bindingResult.hasErrors()) 
			return novo(cidade);

		try {
			cidadeService.salvar(cidade);
			redirectAttributes.addFlashAttribute("mensagem", "Cidade cadastrada com sucesso!");
		} catch (CidadeJaCadastradaException e) {
			bindingResult.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(cidade);
		}

		return new ModelAndView("redirect:/cidades");
	}

}
