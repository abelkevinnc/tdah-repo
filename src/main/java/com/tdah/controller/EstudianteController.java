package com.tdah.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.dto.EstudianteDTO;
import com.tdah.model.Estudiante;
import com.tdah.service.IEstudianteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/estudiante")
@Slf4j
public class EstudianteController {
	@Autowired
	IEstudianteService estudianteService;
	
	@GetMapping("/listar")
	public ModelAndView listarEstudiantes() {
		ModelAndView model = new ModelAndView("estudiante/listar-estudiante");
		log.info("list estudiantes");
		List<EstudianteDTO> estudiantes = estudianteService.listarEstudiantesFront();
		
		model.addObject("estudiantes", estudiantes);
		return model;
	}
	
	
	@GetMapping("/registrar")
	public String registrarEstudiante(Map<String, Object> model) {
		Estudiante estudiante = new Estudiante();
		estudiante.setPrimerNombre("Raul");
		model.put("estudiante", estudiante);
		return "estudiante/registrar-estudiante";
	}
	
	@PostMapping("/registrar")
	public String guardar(@Valid Estudiante estudiante, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		try {
			estudianteService.saveOrUpdate(estudiante);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}

}
