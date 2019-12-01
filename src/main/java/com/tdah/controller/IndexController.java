package com.tdah.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tdah.model.Usuario;
import com.tdah.service.IUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {
	
	@Autowired
	IUsuarioService usuarioService;

	@GetMapping(value = { "/home", "/" })
	public String openHomePage(Map<String, Object> model, HttpSession session) {
		log.info("Index controller");
		
		String nombreUsuario = "";
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails userDetails = null;
			if (principal instanceof UserDetails) {
			  userDetails = (UserDetails) principal;
			  nombreUsuario = userDetails.getUsername();
			  log.info("El usuario " + nombreUsuario + " acaba de iniciar sesión");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!nombreUsuario.equals("")) {
			Usuario usuarioSesion = usuarioService.getUserByUsername(nombreUsuario);
			session.setAttribute("usuarioSesion", usuarioSesion);
			model.put("usuarioSesion", usuarioSesion);
			return "index";
		}

		return "redirect:/autenticacion/login";

	}

}
