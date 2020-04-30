package com.mj.brewer.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mj.brewer.controller.page.PageWrapper;
import com.mj.brewer.controller.validator.VendaValidator;
import com.mj.brewer.mail.Mailer;
import com.mj.brewer.model.Cerveja;
import com.mj.brewer.model.ItemVenda;
import com.mj.brewer.model.StatusVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.model.filter.VendaFilter;
import com.mj.brewer.model.security.UsuarioAutenticado;
import com.mj.brewer.repository.Cervejas;
import com.mj.brewer.service.VendaService;
import com.mj.brewer.session.TabelasItemVenda;

@Controller
@RequestMapping("/vendas")
public class VendasController {

	public static final String VIEW_PESQUISA = "vendas/pesquisa";
	public static final String VIEW_NOVA_VENDA = "vendas/nova-venda";
	public static final String VIEW_NOVA_VENDA_ITEM = "vendas/nova-venda-item";

	@Autowired
	private Cervejas cervejas;

	@Autowired
	private TabelasItemVenda tabelasItemVenda;

	@Autowired
	private VendaService vendaService;

	@Autowired
	private VendaValidator vendaValidador;

	@Autowired
	private Mailer mailer;

	@InitBinder("venda")
	private void inicializarValidador(WebDataBinder webDataBinder) {
		webDataBinder.setValidator(vendaValidador);
	}

	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter, @PageableDefault(size = PageWrapper.DEFAULT_PAGE_SIZE) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(VIEW_PESQUISA);
		mv.addObject("pagina", vendaService.filtrar(vendaFilter, pageable, httpServletRequest));
		mv.addObject("todosStatus", StatusVenda.values());

		return mv;
	}

	@GetMapping("/nova")
	public ModelAndView novaVenda(Venda venda) {
		ModelAndView mv = new ModelAndView(VIEW_NOVA_VENDA);
		mv.addObject("venda", venda);
		mv.addObject("total", Optional.ofNullable(venda.getSubTotal()).orElse(BigDecimal.ZERO));

		if (venda.getUuid() == null)
			venda.setUuid(UUID.randomUUID());
		else
			mv.addObject("itens", tabelasItemVenda.getItens(venda.getUuid().toString()));

		return mv;
	}

	@GetMapping("/cancelar/{id}")
	public ModelAndView cancelar(@PathVariable("id") Venda venda, @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		boolean isAdmin = usuarioAutenticado.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

		if (venda.getVendedor().equals(usuarioAutenticado.getUsuario()) || isAdmin) {
			venda.setStatusVenda(StatusVenda.CANCELADA);
			vendaService.salvar(venda);
		} else {
			return new ModelAndView("redirect:/403");
		}

		return new ModelAndView("redirect:/vendas");
	}

	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Venda venda = vendaService.buscarComItens(id);

		venda.setUuid(UUID.randomUUID());
		for (ItemVenda iv : venda.getItens())
			tabelasItemVenda.adicionarItem(venda.getUuid().toString(), iv.getCerveja(), iv.getQuantidade());

		return novaVenda(venda);
	}

	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult errors, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UsuarioAutenticado usuario) {
		salvarVendaHeader(venda, errors, usuario);

		// se encontrou algum erro
		if (errors.hasErrors())
			return novaVenda(venda);

		venda = vendaService.salvar(venda);

		// salva a venda
		return vendaFooter(venda, redirectAttributes, String.format("Venda nº: %d salva com sucesso!", venda.getId()));
	}

	@PostMapping(value = "/nova", params = "salvarEmitir")
	public ModelAndView salvarEmitir(Venda venda, BindingResult errors, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UsuarioAutenticado usuario) {
		salvarVendaHeader(venda, errors, usuario);

		// se encontrou algum erro
		if (errors.hasErrors())
			return novaVenda(venda);

		venda = vendaService.salvarEmitir(venda);

		// salva a venda
		return vendaFooter(venda, redirectAttributes, String.format("Venda nº: %d realizada e emitida com sucesso!", venda.getId()));
	}

	@PostMapping(value = "/nova", params = "salvarEmail")
	public ModelAndView salvarEmail(Venda venda, BindingResult errors, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UsuarioAutenticado usuario) {

		salvarVendaHeader(venda, errors, usuario);

		// se encontrou algum erro
		if (errors.hasErrors())
			return novaVenda(venda);

		try {
			mailer.enviar(venda = vendaService.salvarEmail(venda));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// salva a venda
		return vendaFooter(venda, redirectAttributes,
				String.format("Venda nº: %d realizada e email foi enviado com sucesso!", venda.getId()));
	}

	private void salvarVendaHeader(Venda venda, BindingResult errors, UsuarioAutenticado usuario) {
		venda.setVendedor(usuario.getUsuario());
		venda.adicionarItens(tabelasItemVenda.getItens(venda.getUuid().toString()));

		// valida a venda
		vendaValidador.validate(venda, errors);
	}

	private ModelAndView vendaFooter(Venda venda, RedirectAttributes redirectAttributes, String successMessage) {
		redirectAttributes.addFlashAttribute("mensagem", successMessage);

		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping("/item")
	public ModelAndView adicionarItem(Long cervejaID, String uuid, Long vendaId) {
		Venda venda = vendaService.buscar(vendaId).orElse(new Venda());
		Optional<Cerveja> optional = cervejas.findById(cervejaID);

		// verifica se a cerveja existe
		if (optional.isPresent())
			tabelasItemVenda.adicionarItem(uuid, optional.get(), BigDecimal.ONE);

		return mvItemVenda(uuid, venda);
	}

	@PutMapping("/item/{cervejaID}")
	public ModelAndView atualizarItem(@PathVariable("cervejaID") Cerveja cerveja, String uuid, int quantidade, Long vendaId) {
		System.out.println("Venda ID: " + vendaId);
		Venda venda = vendaService.buscar(vendaId).orElse(new Venda());
		
		if (cerveja != null)
			tabelasItemVenda.atualizarQuantidadeItem(uuid, cerveja, BigDecimal.valueOf(quantidade));

		return mvItemVenda(uuid, venda);
	}

	@DeleteMapping("/item/{uuid}/{cervejaID}/{vendaId}")
	public ModelAndView deletarItem(@PathVariable String uuid, @PathVariable("cervejaID") Cerveja cerveja, @PathVariable("vendaId") Long vendaId) {
		System.out.println("Venda ID: " + vendaId);
		Venda venda = vendaService.buscar(vendaId).orElse(new Venda());
		
		if (cerveja != null)
			tabelasItemVenda.excluirItem(uuid, cerveja);

		return mvItemVenda(uuid, venda);
	}

	@GetMapping("/mostrarCarrinho/{uuid}")
	@ResponseBody
	public List<ItemVenda> mostrarCarrinho(@PathVariable String uuid) {
		return tabelasItemVenda.getItens(uuid);
	}

	private ModelAndView mvItemVenda(String uuid, Venda venda) {
		ModelAndView mv = new ModelAndView(VIEW_NOVA_VENDA_ITEM);
		mv.addObject("venda", venda);
		mv.addObject("itens", tabelasItemVenda.getItens(uuid));
		mv.addObject("total", tabelasItemVenda.getValorTotal(uuid));
		return mv;
	}
}
