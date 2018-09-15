package com.mj.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Cliente;
import com.mj.brewer.model.TipoPessoa;
import com.mj.brewer.model.filter.ClienteFilter;
import com.mj.brewer.repository.Estados;
import com.mj.brewer.service.ClienteService;
import com.mj.brewer.service.exception.ClienteJaCadastradoException;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

	public static final String VIEW_CADASTRO = "clientes/cadastro";
	public static final String VIEW_PESQUISA = "clientes/pesquisa";

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private Estados estados;

	@GetMapping()
	public ModelAndView pesquisa(ClienteFilter clienteFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpRequest) {
		ModelAndView mv = new ModelAndView(VIEW_PESQUISA);

		// busca todas as cervejas utilizando filtro e paginação
		mv.addObject("pagina", clienteService.clientes(clienteFilter, pageable, httpRequest));

		return mv;
	}

	@GetMapping("/novo")
	public ModelAndView cadastro(Cliente cliente) {
		ModelAndView mv = new ModelAndView(VIEW_CADASTRO);
		mv.addObject("tipoPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());

		return mv;
	}

	@PostMapping()
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		// se encontrou algum erro de validação
		if (bindingResult.hasErrors())
			return cadastro(cliente);

		try {
			// salva o cliente
			clienteService.salvar(cliente);
		} catch (ClienteJaCadastradoException e) {
			bindingResult.rejectValue("cpfCnpj", e.getMessage(), e.getMessage());
			return cadastro(cliente);
		}

		redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
		return new ModelAndView("redirect:/clientes/novo");
	}

	@GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cliente> pesquisaRapida(String nome) {
		validarNome(nome);
		return clienteService.pesquisarPorNome(nome);
	}

	private void validarNome(String nome) {
		if (StringUtils.isNullOrEmpty(nome) || nome.length() < 3)
			throw new IllegalArgumentException("Informe pelo menos 3 caracteres!");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
