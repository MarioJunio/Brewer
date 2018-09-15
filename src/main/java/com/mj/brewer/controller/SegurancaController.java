package com.mj.brewer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SegurancaController {

	public static final String LOGIN_VIEW = "login";

	@GetMapping("/login")
	public String autenticar(@AuthenticationPrincipal User user) {

		if (user != null)
			return "redirect:/cervejas";

		return LOGIN_VIEW;
	}

	@RequestMapping("/403")
	public String _403() {
		return "403";
	}

	@RequestMapping("/404")
	public String _404() {
		return "404";
	}

	@RequestMapping("/500")
	public String _500() {
		return "500";
	}
}
