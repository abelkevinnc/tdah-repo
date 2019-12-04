package com.tdah.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tdah.model.Usuario;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/usuario")
@Slf4j
public class UsuarioController {

	@GetMapping("/listar")
	public String listarUsuarios(Map<String, Object> model, HttpSession session) {
		log.info("usuario controller: registrar usuario");
		if(session.getAttribute("usuarioSesion") != null) {
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "usuario/listar-usuario";
		}
		
		return "autenticacion/login";
		
	}
	
}
