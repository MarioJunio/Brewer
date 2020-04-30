package com.mj.brewer.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Estilo;
import com.mj.brewer.model.filter.EstiloFilter;
import com.mj.brewer.service.EstiloService;
import com.mj.brewer.service.exception.EstiloCadastradoException;
import com.mj.brewer.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

	public static final String VIEW_CADASTRO = "/estilos/cadastro";
	public static final String VIEW_PESQUISA = "/estilos/pesquisa";

	@Autowired
	private EstiloService estiloService;

	@GetMapping
	public ModelAndView pesquisa(EstiloFilter estiloFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(VIEW_PESQUISA);
		mv.addObject("pagina", estiloService.pesquisa(estiloFilter, pageable, httpServletRequest));

		return mv;
	}

	@GetMapping("/cadastro")
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView(VIEW_CADASTRO);
		mv.addObject("estilo", estilo);
		
		return mv;
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(@Valid Estilo estilo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		// verifica se tem erros na validação
		if (bindingResult.hasErrors())
			return novo(estilo);

		try {
			estiloService.salvar(estilo);
			redirectAttributes.addFlashAttribute("mensagem", "Estilo cadastrado com sucesso");
		} catch (EstiloCadastradoException e) {
			bindingResult.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}

		return new ModelAndView("redirect:/estilos");
	}

	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Estilo> buscar = estiloService.buscar(id);
		return novo(buscar.isPresent() ? buscar.get() : new Estilo());
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
		Optional<Estilo> estilo = estiloService.buscar(id);

		try {
			estiloService.excluir(estilo.get());
			return ResponseEntity.ok(estilo.get().getNome() + " foi excluído!");
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping(value = "/salvarModal", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvarModal(@RequestBody @Valid Estilo estilo, BindingResult result) {

		System.out.println("/salvarModal");

		if (result.hasErrors())
			return ResponseEntity.badRequest().body(result.getFieldError(Estilo.NOME).getDefaultMessage());

		return ResponseEntity.ok(estiloService.salvar(estilo));
	}
}
