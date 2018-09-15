package com.mj.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.model.Usuario;
import com.mj.brewer.model.UsuarioStatus;
import com.mj.brewer.model.filter.UsuarioFilter;
import com.mj.brewer.repository.Grupos;
import com.mj.brewer.service.UsuarioService;
import com.mj.brewer.service.exception.EmailJaCadastradoException;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	public static final String VIEW_CADASTRO = "usuarios/cadastro";
	public static final String VIEW_CONSULTA = "usuarios/pesquisa";

	@Autowired
	private Grupos grupos;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ModelAndView pesquisa(UsuarioFilter usuarioFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(VIEW_CONSULTA);
		mv.addObject("grupos", grupos.findAll());
		mv.addObject("pagina", usuarioService.usuarios(usuarioFilter, pageable, httpServletRequest));
		mv.addObject("usuarioStatus", UsuarioStatus.values());

		return mv;
	}

	@GetMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView(VIEW_CADASTRO);
		mv.addObject("grupos", grupos.findAll());
		mv.addObject("usuario", usuario);

		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		ModelAndView mv = novo(usuarioService.buscarComGrupos(id));
		return mv;
	}

	@PostMapping
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		System.out.println(usuario.toString());
		
		if (bindingResult.hasErrors())
			return novo(usuario);

		try {
			usuarioService.salvar(usuario);
			redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
		} catch (EmailJaCadastradoException e) {
			bindingResult.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		}

		return new ModelAndView("redirect:/usuarios/novo");
	}

	@PutMapping("/alterarStatus")
	@ResponseStatus(HttpStatus.OK)
	public void alterarStatus(@RequestParam("codigos[]") Long[] codigos, @RequestParam("status") UsuarioStatus usuarioStatus) {
		usuarioService.alterarStatus(codigos, usuarioStatus);
	}
}
