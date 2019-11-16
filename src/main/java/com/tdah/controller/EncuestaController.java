package com.tdah.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import com.tdah.model.Encuesta;
import com.tdah.model.Estudiante;
import com.tdah.model.InstitucionEducativa;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IInstitucionEducativaService;
import com.tdah.util.ListaItems;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/encuesta")
@Slf4j
public class EncuestaController {
	
	@Autowired
	IInstitucionEducativaService institucionEducativaService;
	
	@Autowired
	IEncuestaService encuestaService;
	
	@GetMapping("/registrar")
	public ModelAndView vistaEncuesta() {
		log.info("registrar encuesta");
		List<InstitucionEducativa> institucionEducativas = institucionEducativaService.findAll();
		
		List<Encuesta> encuestas = encuestaService.findAll();
		List<Encuesta> encuestasEnproceso = encuestas.stream().filter(e -> e.getEstado().equalsIgnoreCase("EN PROCESO")).collect(Collectors.toList());	
		
		Encuesta encuesta = new Encuesta();
		
		ModelAndView model = new ModelAndView("encuesta/registrar-encuesta");		
		model.addObject("institucionEducativas", institucionEducativas);
		model.addObject("encuestasEnproceso", encuestasEnproceso);
		model.addObject("encuesta", encuesta);
		return model;
	}
	
	@PostMapping("/registrar")
	public String registrarEncuesta(@Valid Encuesta encuesta, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
	
		log.info("registrar encuesta");
		log.info(""+ encuesta.getInstitucionEducativa().getDenominacion());
		
		encuesta.setEstado("EN PROCESO");
		encuesta.setFechaCreacion(new Date());
		
		try {
			encuestaService.saveOrUpdate(encuesta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	
	
	@GetMapping("/registrar-cuestionario")
	public ModelAndView registrarCuestionario() {
		log.info("registrar encuesta");
		ModelAndView model = new ModelAndView("encuesta/registrar-cuestionario");
		ListaItems cuestionario1 = new ListaItems();
		cuestionario1.setNumeroItem(1);
		cuestionario1.setDescripcionItem("Descripcion 1");
		
		ListaItems cuestionario2 = new ListaItems();
		cuestionario2.setNumeroItem(2);
		cuestionario2.setDescripcionItem("Descripcion 2");
		
		List<ListaItems> lista = new ArrayList<>();
		lista.add(cuestionario1);
		lista.add(cuestionario2);
		
		model.addObject("listas", lista);
		return model;
	}
	

}
