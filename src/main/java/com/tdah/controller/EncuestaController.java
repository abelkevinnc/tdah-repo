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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.model.Estudiante;
import com.tdah.model.InstitucionEducativa;
import com.tdah.model.ResultadoEncuesta;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IInstitucionEducativaService;
import com.tdah.service.IProfesorService;
import com.tdah.util.Items;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/encuesta")
@Slf4j
public class EncuestaController {
	
	@Autowired
	IInstitucionEducativaService institucionEducativaService;
	
	@Autowired
	IEncuestaService encuestaService;
	
	@Autowired
	IEstudianteService estudianteService;
	
	@Autowired
	IProfesorService profesorService;
	
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
	
	
	@GetMapping("/registrar-cuestionario/{codEncuesta}")
	public ModelAndView vistaCuestionario(@PathVariable(value = "codEncuesta") int codEncuesta) {
		log.info("formulario encuesta con id: "+ codEncuesta);
		
		Items items = new Items();	
		List<ResultadoEncuesta> resultadoEncuestas = new ArrayList<ResultadoEncuesta>();
		
		for(Items i : items.listaItems()) {
			resultadoEncuestas.add(new ResultadoEncuesta(i.getNumeroItem(), i.getDescripcionItem()));
		}	
		
		DetalleEncuesta detalleEncuesta = new DetalleEncuesta();
		detalleEncuesta.setResultadoEncuestas(resultadoEncuestas);
		detalleEncuesta.setCodEncuesta(codEncuesta);
		
		ModelAndView model = new ModelAndView("encuesta/registrar-cuestionario");
		model.addObject("detalleEncuesta", detalleEncuesta);
		model.addObject("estudiantes", estudianteService.findAll());
		model.addObject("profesores", profesorService.findAll());
		model.addObject("resultadoEncuestas", resultadoEncuestas);
		model.addObject("mensaje_exito_cuestionario", "Cuestionario registrado exitosamente.");
		

		return model;
	}
	
	
	@PostMapping("/registrar-cuestionario")
	public String registrarCuestionario(@Valid DetalleEncuesta detalleEncuesta, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		log.info("registrar encuesta con id: "+ detalleEncuesta.getCodEncuesta());
		int codEncuesta = detalleEncuesta.getCodEncuesta();
		detalleEncuesta.setFechaAplicacion(new Date());
		Encuesta encuesta = encuestaService.findById(codEncuesta);
		encuesta.agregarDetalleEncuesta(detalleEncuesta);
		try {
			encuestaService.saveOrUpdate(encuesta);
			flash.addAttribute("mensaje_exito_cuestionario", "Cuestionario registrado exitosamente.");
		} catch (Exception e) {
			e.printStackTrace();
			//flash.addAttribute("mensaje_exito_cuestionario", "Vuelva a intentarlo");
		}
		
		
		
//		try {
//			log.info("list size: "+ detalleEncuesta.getResultadoEncuestas());
//			if(!detalleEncuesta.getResultadoEncuestas().isEmpty()) {
//				for(ResultadoEncuesta re : detalleEncuesta.getResultadoEncuestas()) {
//					log.info(""+re.getNumeroItem());
//					log.info(""+re.getRespuestaItem());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		return "redirect:/";
	}

}
