package com.tdah.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.tdah.dto.ProfesorDTO;
import com.tdah.model.Contacto;
import com.tdah.model.Estudiante;
import com.tdah.model.Profesor;
import com.tdah.model.Usuario;
import com.tdah.service.IProfesorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/profesor")
@Slf4j
public class ProfesorController {
	@Autowired
	IProfesorService profesorService;

	@GetMapping("/listar")
	public String listarProfesores(Map<String, Object> model, HttpSession session) {
		log.info("Profesor controller: listar profesores");
		
		if(session.getAttribute("usuarioSesion") != null) {
			List<ProfesorDTO> profesores = profesorService.listarProfesoresFront();
			model.put("profesores", profesores);
			model.put("usuarioSesion",(Usuario) session.getAttribute("usuarioSesion"));
			return "profesor/listar-profesor";
		}
		return "autenticacion/login";
	}

	@GetMapping("/registrar")
	public String getRegistrarProfesor(Map<String, Object> model, HttpSession session) {
		log.info("Profesor controller: registrar profesor");
		
		if(session.getAttribute("usuarioSesion") != null) {
			Profesor profesor = new Profesor();

			List<Contacto> contactos = new ArrayList<Contacto>();
	
			Contacto contacto = new Contacto();
			contacto.setDireccion("");
			contacto.setCorreoElectronico("");
			contacto.setNumeroTelefonico("");
	
			contactos.add(contacto);
			
			profesor.setContactos(contactos);
			model.put("profesor", profesor);
			model.put("contactos", contactos);
			model.put("usuarioSesion",(Usuario) session.getAttribute("usuarioSesion"));
			
			return "profesor/registrar-profesor";
		}
		
		return "autenticar/login";
		
	}

	@PostMapping("/registrar")
	public String postRegistrarProfesor(@Valid Profesor profesor, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		try {
			profesorService.saveOrUpdate(profesor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editarEstudiante(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash, HttpSession session) {
		log.info("Profesor controller: editar profesor");
		if(session.getAttribute("usuarioSesion") != null) {
			Profesor profesor = profesorService.findById(id);
		
			if (profesor == null) {
				flash.addFlashAttribute("error", "El profesor no existe en la base de datos");
				return "redirect:/listar";
			}
			model.put("profesor", profesor);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "profesor/editar-profesor";
		}
		
		return "autenticacion/login";
	}
	
	@RequestMapping(value = "/profesorById", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> postProfesorById(HttpServletRequest r) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Profesor controller: profesor by id");
		try {
			Profesor profesor = profesorService.findById(Integer.parseInt(r.getParameter("codProfesor")));
			map.put("profesor", profesor);
			map.put("status", "true");
		} catch (Exception e) {
			map.put("status", "false");
		}
		
		return map;
	
	}

}
