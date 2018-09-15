package com.mj.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.dto.CervejaDTO;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.Origem;
import com.mj.brewer.model.Sabor;
import com.mj.brewer.model.filter.CervejaFilter;
import com.mj.brewer.repository.Estilos;
import com.mj.brewer.service.CervejaService;
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {

	public static final String VIEW_CADASTRO = "cervejas/cadastro";
	public static final String VIEW_PESQUISA = "cervejas/pesquisa";

	@Autowired
	private Estilos estilosRep;

	@Autowired
	private CervejaService cervejaService;

	@GetMapping("/cadastro")
	public ModelAndView novo(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView(VIEW_CADASTRO);
		mv.addObject("estilos", estilosRep.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());

		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable("id") Cerveja cerveja) {
		ModelAndView mv = novo(cerveja);
		mv.addObject("cerveja", cerveja);
		
		return mv;
	}
	
	@GetMapping()
	public ModelAndView pesquisa(CervejaFilter cervejaFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpRequest) {
		ModelAndView mv = new ModelAndView(VIEW_PESQUISA);
		mv.addObject("estilos", estilosRep.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());

		// busca todas as cervejas utilizando filtro e paginação
		mv.addObject("pagina", cervejaService.cervejas(cervejaFilter, pageable, httpRequest));

		return mv;
	}

	@PostMapping(value = { "/salvar", "/{\\d+}" })
	public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors())
			return novo(cerveja);

		cervejaService.salvar(cerveja);
		redirectAttributes.addFlashAttribute("mensagem", "Cerveja cadastrada com sucesso!");
		return new ModelAndView("redirect:/cervejas");
	}

	@GetMapping("/pesquisaRapida")
	public @ResponseBody List<CervejaDTO> pesquisaRapida(String nomeOuSKU) {
		return cervejaService.cervejas(nomeOuSKU);
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> excluir(@PathVariable("id") Cerveja cerveja) {

		try {
			cervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok(cerveja.getNome() + " excluída com sucesso!");
	}

}
