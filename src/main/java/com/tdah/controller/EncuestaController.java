package com.tdah.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tdah.util.ListaItems;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/encuesta")
@Slf4j
public class EncuestaController {
	@GetMapping("/registrar")
	public ModelAndView registrarEncuesta() {
		log.info("registrar encuesta");
		ModelAndView model = new ModelAndView("encuesta/registrar-encuesta");		
		return model;
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
