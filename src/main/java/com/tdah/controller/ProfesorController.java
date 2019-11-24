package com.tdah.controller;

import java.util.ArrayList;
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

import com.tdah.dto.ProfesorDTO;
import com.tdah.model.Contacto;
import com.tdah.model.Profesor;
import com.tdah.service.IProfesorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/profesor")
@Slf4j
public class ProfesorController {
	@Autowired
	IProfesorService profesorService;

	@GetMapping("/listar")
	public ModelAndView listarProfesores() {
		ModelAndView model = new ModelAndView("profesor/listar-profesor");
		log.info("list profesores");
		List<ProfesorDTO> profesores = profesorService.listarProfesoresFront();
		model.addObject("profesores", profesores);
		return model;
	}

	@GetMapping("/registrar")
	public String registrarProfesor(Map<String, Object> model) {
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
		return "profesor/registrar-profesor";
	}

	@PostMapping("/registrar")
	public String guardar(@Valid Profesor profesor, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		try {
			profesorService.saveOrUpdate(profesor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:listar";
	}

}
