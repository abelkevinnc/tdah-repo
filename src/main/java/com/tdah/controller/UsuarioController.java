package com.tdah.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.tdah.model.Rol;
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
			Rol rol = new Rol();
			List<Contacto> contactos = new ArrayList<Contacto>();
			
			Contacto contacto = new Contacto();
			contacto.setDireccion("");
			contacto.setCorreoElectronico("");
			contacto.setNumeroTelefonico("");
			
			contactos.add(contacto);
			
			usuario.setContactos(contactos);
			usuario.setRol(rol);
			usuario.setClave("sistematdah");//clave por defecto
			model.put("usuario", usuario);
			model.put("contactos", contactos);
			model.put("rol", rol);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "usuario/registrar-usuario";
		}
		
		return "autenticacion/login";
		
	}
	
	@PostMapping("/registrar")
	public String postRegistrarUsuario(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		log.info("Usuario controller: registrar usuario POST");
		try {
			usuario.setFechaRegistro(new Date());
			
			String clave = usuario.getClave();
			String claveEncriptada = encoder.encode(clave);
			usuario.setClave(claveEncriptada);
			
			Rol rol = usuario.getRol();
			rol.setFechaRegistro(new Date());
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 30);  
			
			rol.setFechaRenovacion(calendar.getTime());
			
			usuarioService.saveOrUpdate(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}
	
}
