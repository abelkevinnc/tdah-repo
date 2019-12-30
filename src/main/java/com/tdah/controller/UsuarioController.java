package com.tdah.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.model.Contacto;
import com.tdah.model.Usuario;
import com.tdah.service.IUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/usuario")
@Slf4j
public class UsuarioController {
	
	@Autowired
	IUsuarioService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@GetMapping("/listar")
	public String listarUsuarios(Map<String, Object> model, HttpSession session) {
		log.info("usuario controller: listar usuario");
		if(session.getAttribute("usuarioSesion") != null) {
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "usuario/listar-usuario";
		}
		
		return "autenticacion/login";
		
	}
	
	@GetMapping("/registrar")
	public String getRegistrarUsuario(Map<String, Object> model, HttpSession session) {
		log.info("Usuario controller: registrar usuario GET");
		
		if(session.getAttribute("usuarioSesion") != null) {
			
			Usuario usuario = new Usuario();
			List<Contacto> contactos = new ArrayList<Contacto>();
			
			Contacto contacto = new Contacto();
			contacto.setDireccion("");
			contacto.setCorreoElectronico("");
			contacto.setNumeroTelefonico("");
			
			contactos.add(contacto);
			
			usuario.setContactos(contactos);
			usuario.setClave("sistematdah");//clave por defecto
			model.put("usuario", usuario);
			model.put("contactos", contactos);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "usuario/registrar-usuario";
		}
		
		return "autenticacion/login";
		
	}
	
	@PostMapping("/registrar")
	public String postRegistrarUsuario(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		log.info("Usuario controller: registrar usuario POST");
		try {
			String clave = usuario.getClave();
			String claveEncriptada = encoder.encode(clave);
			usuario.setClave(claveEncriptada);
			usuarioService.saveOrUpdate(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}
	
}
