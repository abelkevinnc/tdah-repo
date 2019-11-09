package com.tdah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tdah.dto.EstudianteDTO;
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

}
