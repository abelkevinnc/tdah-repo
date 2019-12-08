package com.tdah.restcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.model.Estudiante;
import com.tdah.model.InstitucionEducativa;
import com.tdah.model.Profesor;
import com.tdah.model.ResultadoEncuesta;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IInstitucionEducativaService;
import com.tdah.service.IProfesorService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api")
@Slf4j
public class LoadDataRestController {
	
	@Autowired
	IEstudianteService estudianteService;
	@Autowired
	IEncuestaService encuestaService;
	@Autowired
	IProfesorService profesorService;
	
	@Autowired
	IInstitucionEducativaService institucionEducativaService;
	
	@GetMapping("/loadData")
	public void load() {
	
//		InstitucionEducativa institucionEducativa = new InstitucionEducativa();
//		
//		institucionEducativa.setCodigoModular("422063");
//		institucionEducativa.setDenominacion("38001 GUSTAVO CASTRO PANTOJA");
//		institucionEducativa.setNivelModalidad("Primaria");
//		institucionEducativa.setGestionDependencia("Pública - Sector Educación");
//		institucionEducativa.setDireccion("JIRON GARCILAZO DE LA VEGA 111");
//		institucionEducativa.setLugar("Ayacucho / Huamanga / Ayacucho");
		
		InstitucionEducativa newIE = institucionEducativaService.findById(1);
		
		Encuesta encuesta = new Encuesta();
		encuesta.setDenominacion("DSM V");
		encuesta.setFechaCreacion(new Date());
		encuesta.setFechaFinalizacion(new Date());
		encuesta.setInstitucionEducativa(newIE);
		encuesta.setEstado("EN PROCESO");
		
		List<DetalleEncuesta> detalleEncuestas = dataDetalleEncuesta();
		
		encuesta.setDetalleEncuestas(detalleEncuestas);
		encuestaService.saveOrUpdate(encuesta);
		
	}
	
	public List<Profesor> dataProfesor(int total) {
		List<Profesor> listProfesores = new ArrayList<Profesor>();
		for(int i = 1; i <= total; i++) {
			Map<String, Object> randomData = generateRandomData();
			
			Profesor profesor = new Profesor();
			
			profesor.setPrimerNombre("primernombre_profesor_"+i);
			profesor.setSegundoNombre("segundonombre_profesor_"+i);
			profesor.setApellidoPaterno("apellidopaterno_profesor_"+i);
			profesor.setApellidoMaterno("apellidomaterno_profesor_"+i);
			profesor.setTipoDocumento("DNI");
			profesor.setNumeroDocumento("" + randomData.get("randomDni"));
			profesor.setFechaNacimiento(new Date());
			profesor.setGenero(randomData.get("randomGenero").toString());
			profesor.setCodigoDocente("" + randomData.get("randomDni"));
			
			listProfesores.add(profesor);
		}
		
		return listProfesores;
	}
	
	public List<Estudiante> dataEstudiante(int total, int numeroOrden) {
		List<Estudiante> listEstudiantes = new ArrayList<Estudiante>();
		for(int i = 1; i <= total; i++) {
			Map<String, Object> randomData = generateRandomData();
			
			Estudiante estudiante = new Estudiante();
			
			estudiante.setPrimerNombre("primernombre_estudiante_"+numeroOrden);
			estudiante.setSegundoNombre("segundonombre_estudiante_"+numeroOrden);
			estudiante.setApellidoPaterno("apellidopaterno_estudiante_"+numeroOrden);
			estudiante.setApellidoMaterno("apellidomaterno_estudiante_"+numeroOrden);
			estudiante.setTipoDocumento("DNI");
			estudiante.setNumeroDocumento("" + randomData.get("randomDni"));
			estudiante.setFechaNacimiento(new Date());
			estudiante.setGenero(randomData.get("randomGenero").toString());
			estudiante.setTipoFamilia(randomData.get("randomTipoFamilia").toString());
			estudiante.setEstado("ACTIVO");
			
			listEstudiantes.add(estudiante);
			numeroOrden++;
		}
		
		return listEstudiantes;
	}
	
	public Map<String, Object> generateRandomData() {
		Map<String, Object> randomData = new HashMap<String, Object>();
		
		int randomDni = new Random().nextInt(80000000 - 20000000) + 20000000;
		int randomGenero = new Random().nextInt(2);
		
		randomData.put("randomDni", randomDni);
		randomData.put("randomGenero", (randomGenero == 0) ? "F" : "M");
		
		String randomTipoFamilia = "";
		
		switch (new Random().nextInt(3)) {
		case 0:
			randomTipoFamilia = "A";
			break;
		case 1:
			randomTipoFamilia = "B";
			break;
		case 2:
			randomTipoFamilia = "C";
			break;
		default:
			break;
		}
		
		randomData.put("randomTipoFamilia", randomTipoFamilia);
		
		
		return randomData;
	}
	
	
	public List<DetalleEncuesta> dataDetalleEncuesta() {
		List<DetalleEncuesta> detalleEncuestas =  new ArrayList<DetalleEncuesta>();
		
		List<Profesor> profesores1 = dataProfesor(6);
		int numeroOrden = 1;
		for (int i = 0; i < 6; i++) {
			Profesor prof = profesorService.saveOrUpdate(profesores1.get(i));
			
			int randomCantidadalumnos = new Random().nextInt(12 - 8) + 8;
			List<Estudiante> estudiantes1 = dataEstudiante(randomCantidadalumnos, numeroOrden);
			numeroOrden += randomCantidadalumnos;
			for (int j = 0; j < randomCantidadalumnos; j++) {
				//Estudiante est = estudianteService.saveOrUpdate(estudiantes1.get(j));
				
				
				Estudiante est = estudiantes1.get(j);
				DetalleEncuesta detalleEncuesta = new DetalleEncuesta();
				detalleEncuesta.setProfesor(prof);
				detalleEncuesta.setEstudiante(est);
				detalleEncuesta.setGradoEstudio(i+1+"°");
				detalleEncuesta.setNivelEducacion("PRIMARIA");
				detalleEncuesta.setResultadoEncuestas(dataResultadoEncuesta());
				detalleEncuesta.setFechaAplicacion(new Date());
				
				detalleEncuestas.add(detalleEncuesta);
				
			}
			
		}
		
		return detalleEncuestas;
	}
	
	public List<ResultadoEncuesta> dataResultadoEncuesta() {
		List<ResultadoEncuesta> resultadoEncuestas = new ArrayList<ResultadoEncuesta>();
		
		for (int i = 0; i < 18; i++) {
			ResultadoEncuesta resultadoEncuesta = new ResultadoEncuesta();
			
			resultadoEncuesta.setNumeroItem(i+1);
			resultadoEncuesta.setRespuestaItem((new Random().nextInt(2) == 0) ? "F" : "V");
			
			resultadoEncuestas.add(resultadoEncuesta);
		}
		
		
		return resultadoEncuestas;
	}
	
	
	
	

}
