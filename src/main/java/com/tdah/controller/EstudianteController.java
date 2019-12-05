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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.dto.EstudianteDTO;
import com.tdah.model.Contacto;
import com.tdah.model.Estudiante;
import com.tdah.model.Usuario;
import com.tdah.service.IEstudianteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/estudiante")
@Slf4j
public class EstudianteController {
	@Autowired
	IEstudianteService estudianteService;
	
	@GetMapping("/listar")
	public ModelAndView listarEstudiantes(HttpSession session) {
		log.info("Estudiante controller: listar estudiantes");
		ModelAndView model = null;
		if(session.getAttribute("usuarioSesion") != null) {
			model = new ModelAndView("estudiante/listar-estudiante");
			
			List<EstudianteDTO> estudiantes = estudianteService.listarEstudiantesFront();
			
			model.addObject("estudiantes", estudiantes);
			model.addObject("usuarioSesion",(Usuario) session.getAttribute("usuarioSesion"));
			
		} else {
			model = new ModelAndView("autenticacion/login");
		}
		return model;
	}
	
	
	@GetMapping("/registrar")
	public String getRegistrarEstudiante(Map<String, Object> model, HttpSession session) {
		log.info("Estudiante controller: registrar estudiante");
		
		if(session.getAttribute("usuarioSesion") != null) {
			
			Estudiante estudiante = new Estudiante();
			List<Contacto> contactos = new ArrayList<Contacto>();
			
			Contacto contacto = new Contacto();
			contacto.setDireccion("");
			contacto.setCorreoElectronico("");
			contacto.setNumeroTelefonico("");
			
			contactos.add(contacto);
			
			estudiante.setContactos(contactos);
			
			model.put("estudiante", estudiante);
			model.put("contactos", contactos);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "estudiante/registrar-estudiante";
		}
		
		return "autenticacion/login";
		
	}
	
	@PostMapping("/registrar")
	public String postRegistrarEstudiante(@Valid Estudiante estudiante, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		try {
			estudianteService.saveOrUpdate(estudiante);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editarEstudiante(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash, HttpSession session) {
		log.info("Estudiante controller: editar estudiante");
		if(session.getAttribute("usuarioSesion") != null) {
			Estudiante estudiante = estudianteService.findById(id);
			if (estudiante == null) {
				flash.addFlashAttribute("error", "El estudiante no existe en la base de datos");
				return "redirect:/listar";
			}
			model.put("estudiante", estudiante);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			return "estudiante/editar-estudiante";
		}
		
		return "autenticacion/login";
	}
	
	@RequestMapping(value = "/estudianteById", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> postEstudianteById(HttpServletRequest r) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Estudiante controller: estudiante by id");
		try {
			Estudiante estudiante = estudianteService.findById(Integer.parseInt(r.getParameter("codEstudiante")));
			map.put("estudiante", estudiante);
			map.put("status", "true");
		} catch (Exception e) {
			map.put("status", "false");
		}
		
		return map;
	
	}

}
