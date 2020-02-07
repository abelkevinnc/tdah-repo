package com.tdah.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.tdah.model.Estudiante;
import com.tdah.model.Rol;
import com.tdah.model.Usuario;
import com.tdah.service.ISendSmsService;
import com.tdah.service.IUsuarioService;
import com.tdah.util.DatosRecuperacion;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/usuario")
@Slf4j
public class UsuarioController {
	
	@Autowired
	IUsuarioService usuarioService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	ISendSmsService sendSmsService;

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
	
	@GetMapping("/recuperar-credencial")
	public String recuperarClaveGet(Map<String, Object> model, HttpSession session) {
		log.info("usuario controller: recuperar credencial GET");
		DatosRecuperacion datos = new DatosRecuperacion();
		model.put("datos", datos);
		return "usuario/recuperar-credencial";
		
	}
	
//	@PostMapping("/recuperar-credencial")
//	public String recuperarClavePost(@Valid DatosRecuperacion datos, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
//
//		log.info("Usuario controller: recuperar credencial POST");
//		
//		Usuario usuario = usuarioService.getUserByUsername(datos.getNombreUsuario());
//		
//		UUID uuid = UUID.randomUUID();
//		String randomId = uuid.toString();
//		
//		String codigoEnvio = randomId.substring(randomId.length() - 4).toUpperCase();
//		
//		if(usuario != null) {
//			if(usuario.getContactos().get(0).getNumeroTelefonico().equals(datos.getNumeroTelefono())) {
//				log.info("código de envío: " + codigoEnvio);
//				sendSmsService.enviarSms(codigoEnvio, usuario.getContactos().get(0).getNumeroTelefonico());
//			} else {
//				log.info("numero incorrecto");
//			}
//		} else {
//			log.info("usuario no registrado en la bd");
//		}
//		
//		
//		return "autenticacion/login";
//	}
	
	@RequestMapping(value = "/recuperar-credencial", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> recuperarClavePost(HttpServletRequest r, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Usuario controller: recuperar credencial POST");
		
		String nombreUsuario = r.getParameter("nombreUsuario");
		String numeroTelefono = r.getParameter("numeroTelefono");
		
		Usuario usuario = usuarioService.getUserByUsername(nombreUsuario);
		
		UUID uuid = UUID.randomUUID();
		String randomId = uuid.toString();
		
		String codigoEnvio = randomId.substring(randomId.length() - 4).toUpperCase();
		
		String statusCode = "";
		
		if(usuario != null) {
			if(usuario.getContactos().get(0).getNumeroTelefonico().equals(numeroTelefono)) {
				log.info("código de envío: " + codigoEnvio);
				try {
					sendSmsService.enviarSms(codigoEnvio, usuario.getContactos().get(0).getNumeroTelefonico());
					session.setAttribute("codigoEnvio", codigoEnvio);
					session.setAttribute("usuarioCambio", nombreUsuario);
					map.put("codigoEnvio", codigoEnvio);
					map.put("statusCode", "1");
				} catch (Exception e) {
					map.put("statusCode", "4");
					e.printStackTrace();
				}
				
			} else {
				log.info("numero incorrecto");
				map.put("statusCode", "2");
			}
		} else {
			log.info("usuario no registrado en la bd");
			map.put("statusCode", "3");
		}
		return map;
	
	}
	
	@RequestMapping(value = "/validar-codigo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> validarCodigoPost(HttpServletRequest r, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Usuario controller: validar codigo POST");
		String codigoVerificacion = r.getParameter("codigoVerificacion");
		
		boolean status = false;
		
		if(session.getAttribute("codigoEnvio") != null) {
			String codigoEnvio = (String) session.getAttribute("codigoEnvio");
			if(codigoVerificacion.equals(codigoEnvio)) {
				status = true;
				log.info("CODIGO VALIDO");
			}
		}
		map.put("status", status);
		return map;
	}
	@RequestMapping(value = "/cambiar-clave", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cambiarClavePost(HttpServletRequest r, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Usuario controller: cambiar clave POST");
		String nuevaClave = r.getParameter("nuevaClave");
		
		boolean status = false;
		
		if(session.getAttribute("usuarioCambio") != null) {
			String username = (String) session.getAttribute("usuarioCambio");
			Usuario usuario = usuarioService.getUserByUsername(username);
			
			String claveEncriptada = encoder.encode(nuevaClave);
			usuario.setClave(claveEncriptada);
			
			usuarioService.saveOrUpdate(usuario);
			status = true;
		}

		
		map.put("status", status);
		return map;
	}
	
	
	
}
 