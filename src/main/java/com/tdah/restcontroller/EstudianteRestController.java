package com.tdah.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tdah.dto.EstudianteDTO;
import com.tdah.service.IEstudianteService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
@Slf4j
public class EstudianteRestController {
	@Autowired
	IEstudianteService estudianteService;
	
	@GetMapping("/estudiantes")
	public List<EstudianteDTO> listarEstudiantes() {
		log.info("list estudiantes");
		List<EstudianteDTO> estudiantes = estudianteService.listarEstudiantesFront();

		return estudiantes;
	}
}
