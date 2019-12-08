package com.tdah.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.model.Estudiante;
import com.tdah.model.InstitucionEducativa;
import com.tdah.model.ResultadoEncuesta;
import com.tdah.model.Usuario;
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
	public String getRegistrarEncuesta(Map<String, Object> model, HttpSession session) {
		log.info("Encuesta controller: registrar encuesta");
		if(session.getAttribute("usuarioSesion") != null) {
			List<InstitucionEducativa> institucionEducativas = institucionEducativaService.findAll();
			
			List<Encuesta> encuestas = encuestaService.findAll();
			List<Encuesta> encuestasEnproceso = encuestas.stream().filter(e -> e.getEstado().equalsIgnoreCase("EN PROCESO")).collect(Collectors.toList());	
			List<Encuesta> encuestasFinalizadas = encuestas.stream().filter(e -> e.getEstado().equalsIgnoreCase("FINALIZADO")).collect(Collectors.toList());	
			
			
			Encuesta encuesta = new Encuesta();	
			model.put("institucionEducativas", institucionEducativas);
			model.put("encuestasEnproceso", encuestasEnproceso);
			model.put("encuestasFinalizadas", encuestasFinalizadas);
			model.put("encuesta", encuesta);
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));
			
			return "encuesta/registrar-encuesta";
		}
		
		return "autenticacion/login";
		
	}
	
	@PostMapping("/registrar")
	public String postRegistrarEncuesta(@Valid Encuesta encuesta, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
	
		encuesta.setEstado("EN PROCESO");
		encuesta.setFechaCreacion(new Date());
		try {
			encuestaService.saveOrUpdate(encuesta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:registrar";
	}
	
	
	@GetMapping("/registrar-cuestionario/{codEncuesta}")
	public String vistaCuestionario(@PathVariable(value = "codEncuesta") int codEncuesta, Map<String, Object> model, HttpSession session) {
		log.info("Encuesta controller: registrar cuestionario para la encuesta con id: "+ codEncuesta);
		
		if(session.getAttribute("usuarioSesion") != null) {
			Items items = new Items();	
			List<ResultadoEncuesta> resultadoEncuestas = new ArrayList<ResultadoEncuesta>();
			
			for(Items i : items.listaItems()) {
				resultadoEncuestas.add(new ResultadoEncuesta(i.getNumeroItem(), i.getDescripcionItem()));
			}	
			
			DetalleEncuesta detalleEncuesta = new DetalleEncuesta();
			detalleEncuesta.setResultadoEncuestas(resultadoEncuestas);
			detalleEncuesta.setCodEncuesta(codEncuesta);
			
			model.put("detalleEncuesta", detalleEncuesta);
			model.put("estudiantes", estudianteService.findAll());
			model.put("profesores", profesorService.findAll());
			model.put("resultadoEncuestas", resultadoEncuestas);
			//model.put("mensaje_exito_cuestionario", "Cuestionario registrado exitosamente.");
			model.put("usuarioSesion", (Usuario) session.getAttribute("usuarioSesion"));

			return "encuesta/registrar-cuestionario";
		}
		
		return "autenticacion/login";
		
	}
	
	
	@PostMapping("/registrar-cuestionario")
	public String registrarCuestionario(@Valid DetalleEncuesta detalleEncuesta, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		log.info("Encuesta controller: registrar cuestionario para la encuesta con id: "+ detalleEncuesta.getCodEncuesta());
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
		return "redirect:/encuesta/registrar";
	}
	
	@RequestMapping(value = "/operacion", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> postEstudianteById(HttpServletRequest r) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("Encuesta controller: encuesta operacion");
		try {
			Encuesta encuesta = encuestaService.findById(Integer.parseInt(r.getParameter("codEncuesta")));
			encuesta.setFechaFinalizacion(new Date());
			encuesta.setEstado(r.getParameter("tipoOperacion"));			
			encuestaService.saveOrUpdate(encuesta);
			map.put("status", "true");
		} catch (Exception e) {
			map.put("status", "false");
		}
		
		return map;
	
	}

}
