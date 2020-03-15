package com.tdah.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tdah.model.Encuesta;
import com.tdah.model.Reporte;
import com.tdah.model.Usuario;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IInstitucionEducativaService;
import com.tdah.service.IProfesorService;
import com.tdah.service.IReporteService;

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
	
	@Autowired
	IReporteService reporteService;
	
	@GetMapping("/ver/{codEncuesta}")
	public String verEncuesta(@PathVariable(value = "codEncuesta") Integer codEncuesta, Map<String, Object> model, HttpSession session) {
		log.info("Reporte controller: ver reporte");
		
		if(session.getAttribute("usuarioSesion") != null) {
			Encuesta encuesta = encuestaService.findById(codEncuesta);
			
			//verificar si la encuesta tiene reportes
			List<Reporte> reportes = reporteService.getByEncuestaId(codEncuesta);
			if(reportes.isEmpty()) {
				// generar reportes
				log.info("se procede a generar reportes");
				reporteService.generarReporteSintomasPorGrado(codEncuesta);
				reporteService.generarReporteSintomasPorGenero(codEncuesta);
				reporteService.generarReporteSintomasPorTipoFamilia(codEncuesta);
				
				//se debe eliminar la carpeta temporal
				
				
				String pathTemp = "pathTemp";
				
				File directorio = new File(pathTemp);
				if(directorio.exists()) {
					log.info("El directorio existe.");
					File[] ficheros = directorio.listFiles();
					 
					for (int x=0;x<ficheros.length;x++) {
						ficheros[x].delete();
					}				
					 
					if (directorio.delete())
					 System.out.println("El fichero "+pathTemp+" ha sido borrado correctamente");
					else
					 System.out.println("El fichero no se ha podido borrar");
				}	
				
			}
			
			List<String> urlReportes1 = new ArrayList<>();
			List<String> urlReportes2 = new ArrayList<>();
			List<String> urlReportes3 = new ArrayList<>();
			
			for(Reporte r: reportes) {
				if(r.getCodOrden() == 1) {
					urlReportes1.add("http://localhost:8080/api/encuestas/ver-pdf/"+r.getCodReporte());
				} else if (r.getCodOrden() == 2) {
					urlReportes2.add("http://localhost:8080/api/encuestas/ver-pdf/"+r.getCodReporte());
				} else {
					urlReportes3.add("http://localhost:8080/api/encuestas/ver-pdf/"+r.getCodReporte());
				}
			}
			
			int numEncuestados = encuesta.getDetalleEncuestas().size();
//			String baseUrlheroku = "";
			model.put("encuesta", encuesta);
			model.put("numEncuestados", numEncuestados);
			model.put("usuarioSesion",(Usuario) session.getAttribute("usuarioSesion"));
			model.put("urlReportes1", urlReportes1);
			model.put("urlReportes2", urlReportes2);
			model.put("urlReportes3", urlReportes3);
			
			return "reporte/ver-reporte";
		}
		return "autenticacion/login";
	}

}
