package com.tdah.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	private PasswordEncoder encoder;

	@GetMapping("/listar")
	public String listarUsuarios(Map<String, Object> model, HttpSession session) {
		log.info("usuario controller: listar usuario");
		if(session.getAttribute("usuarioSesion") != null) {
			
			model.put("usuarios", usuarioService.findAll());
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
			//cuando es editar
			
			if(usuario.getCodUsuario() == null) {
				usuario.setEstado("ACTIVO");
				String clave = usuario.getClave();
				String claveEncriptada = encoder.encode(clave);
				usuario.setClave(claveEncriptada);
				usuario.setFechaRegistro(new Date());
				
			} else {
				Usuario usuarioProvisional = usuarioService.findById(usuario.getCodUsuario());
				usuario.setClave(usuarioProvisional.getClave());
				usuario.setFechaRegistro(usuarioProvisional.getFechaRegistro());
//				usuario.setRol(usuarioService.findById(usuario.getCodUsuario()).get);
			}

			
			
			
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DAY_OF_YEAR, 30);  
//			
//			rol.setFechaRenovacion(calendar.getTime());
			
			usuarioService.saveOrUpdate(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editarUsuario(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash, HttpSession session) {
		log.info("Usuario controller: editar usuario");
		if(session.getAttribute("usuarioSesion") != null) {
			Usuario usuario = usuarioService.findById(id);
			
			if (usuario == null) {
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listar";
			}
			Rol rol = usuario.getRol();
			
			List<Contacto> contactos = usuario.getContactos();
			
			model.put("usuario", usuario);
			model.put("rol", rol);
			model.put("contactos", contactos);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "usuario/editar-usuario";
		}
		
		return "autenticacion/login";
	}
	
	@RequestMapping(value = "/usuarioById", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> postUsuarioById(HttpServletRequest r) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Usuario controller: usuario by id");
		try {
			Usuario usuario = usuarioService.findById(Integer.parseInt(r.getParameter("codUsuario")));
			map.put("usuario", usuario);
			map.put("status", "true");
		} catch (Exception e) {
			map.put("status", "false");
		}
		
		return map;
	
	}
	
}
