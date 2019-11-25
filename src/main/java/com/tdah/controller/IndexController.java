package com.tdah.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tdah.model.Usuario;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

	@GetMapping(value = { "/home", "/" })
	public String openHomePage(Map<String, Object> model, HttpSession session) {
		log.info("index");
		if (session.getAttribute("usuario_sesion") != null) {
			Usuario usuario_sesion = (Usuario) session.getAttribute("usuario_sesion");
			model.put("usuario_sesion", usuario_sesion);
			return "index";
		}
		return "redirect:/autenticacion/login";

	}

}
