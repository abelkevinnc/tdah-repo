package com.tdah.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tdah.model.Encuesta;
import com.tdah.model.Usuario;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IInstitucionEducativaService;
import com.tdah.service.IProfesorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/reporte")
@Slf4j
public class ReporteController {
	@Autowired
	IInstitucionEducativaService institucionEducativaService;
	
	@Autowired
	IEncuestaService encuestaService;
	
	@Autowired
	IEstudianteService estudianteService;
	
	@Autowired
	IProfesorService profesorService;
	
	@GetMapping("/ver/{codEncuesta}")
	public String verEncuesta(@PathVariable(value = "codEncuesta") Integer codEncuesta, Map<String, Object> model, HttpSession session) {
		log.info("Reporte controller: ver reporte");
		
		if(session.getAttribute("usuarioSesion") != null) {
			Encuesta encuesta = encuestaService.findById(codEncuesta);
		
			String baseUrllocal = "http://localhost:8080/api/encuestas/ver-pdf/" + codEncuesta;
//			String baseUrlheroku = "";
			model.put("encuesta", encuesta);
			model.put("usuarioSesion",(Usuario) session.getAttribute("usuarioSesion"));
			model.put("baseUrllocal", baseUrllocal);
			
			return "reporte/ver-reporte";
		}
		return "autenticacion/login";
	}

}
