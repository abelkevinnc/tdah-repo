package com.tdah.restcontroller;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orsonpdf.PDFDocument;
import com.orsonpdf.PDFGraphics2D;
import com.orsonpdf.Page;
import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.model.Estudiante;
import com.tdah.model.ResultadoEncuesta;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IReporteService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class GenerateReport {
	
	@Autowired
	IEncuestaService encuestaService;
	
	@Autowired
	IEstudianteService estudianteService;
	
	@Autowired
	IReporteService reporteService;
	

	@GetMapping("/generate-report")
	public void generateReport() throws IOException {
		
		Encuesta encuesta = encuestaService.findAll().get(0);
		List<DetalleEncuesta> detalleEncuestas = encuesta.getDetalleEncuestas();

		reporteService.generarReporteSintomasPorGrado();
		
		// separamos las encuestas por grados
//		Map<String, List<DetalleEncuesta>> encuentas = dividirEncuestasPorGrado(detalleEncuestas);
//		
//		for (int i = 1; i <= 6; i++) {
//			log.info("encuesta"+i);
//			List<DetalleEncuesta> detalleEncuestaPrimerGrado = encuentas.get("encuesta"+i);
//			reportSumSintomas(detalleEncuestaPrimerGrado, i); //reporte sum sintomas
//		}
		
	}
	
	private Map<String, List<DetalleEncuesta>> dividirEncuestasPorGrado(List<DetalleEncuesta> detalleEncuestas) {
		Map<String, List<DetalleEncuesta>> encuentas = new HashMap<>();
		for (int i = 0; i < 6; i++) {
			String grado = (i+1)+"°";
			List<DetalleEncuesta> filtroEncuesta = detalleEncuestas
			.stream()
			.filter(de -> de.getGradoEstudio().equalsIgnoreCase(grado))
			.collect(Collectors.toList());
			encuentas.put("encuesta"+(i+1), filtroEncuesta);
		}
		return encuentas;
	}
	
	private void reportSumSintomas(List<DetalleEncuesta> detalleEncuesta, int grado) { //todos los grados
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//numero de barras
		int numeroBarras = 3;
		
		//numero de alumnos
		int numeroAlumnos = detalleEncuesta.size();
		
		for (int i = 0; i < numeroBarras; i++) {
			for (int j = 0; j < numeroAlumnos; j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				if(i == 0) { //DA
					dataset.addValue(sumSintomas.get("sumDa"), "Deficit de Atencion", "N"+(j+1));
				} else if (i == 1) { //H
					dataset.addValue(sumSintomas.get("sumH"), "Hiperactividad", "N"+(j+1));
				} else {
					dataset.addValue(sumSintomas.get("sumI"), "Impulsividad", "N"+(j+1));
				}
				
			}
			
		}
		
		exportPdf(dataset, grado);
		
	}
	
	public void exportPdf(DefaultCategoryDataset dataset, int grado) {
		JFreeChart barChart = ChartFactory.createBarChart("SUMA SINTOMAS TDAH "+grado+" GRADO", "Sintomas", "Puntaje", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setTitle("Reportes Sistema TDAH");
		pdfDoc.setAuthor("Abel Kevin Nuñez Chavez");

		Page page = pdfDoc.createPage(new Rectangle(612, 468));
		PDFGraphics2D g2 = page.getGraphics2D();

		barChart.draw(g2, new Rectangle(0, 0, 612, 468));
		
		
		String denominacionArchivo = "REPORTE_SUM_SINTOMAS_" + grado; 
		pdfDoc.writeToFile(new File("E:\\"+denominacionArchivo+".pdf"));
	}
	
	public Map<String, Integer> sintomasSeleccionadasPorAlumno(List<ResultadoEncuesta> resultadoEncuestas) {
		Map<String, Integer> sumSintomas = new HashMap<>();
		int sumDa = 0;
		int sumH = 0;
		int sumI = 0;
		
		for (int i = 0; i < 18; i++) {
			if (i < 9 && resultadoEncuestas.get(i).getRespuestaItem().equalsIgnoreCase("V"))  // DA
				sumDa++;
			else if (i >= 9 && i < 15 && resultadoEncuestas.get(i).getRespuestaItem().equalsIgnoreCase("V")) //H
				sumH++;
			else if (i >= 15 && resultadoEncuestas.get(i).getRespuestaItem().equalsIgnoreCase("V")) //I
				sumI++;
		}
		
		sumSintomas.put("sumDa",sumDa);
		sumSintomas.put("sumH",sumH);
		sumSintomas.put("sumI",sumI);
		
		return sumSintomas;
	}
	
	//EXAMPLES
	private void report(List<DetalleEncuesta> detalleEncuesta) { //primer grado
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//numero de barras
		int numeroBarras = 3;
		
		//numero de alumnos
		int numeroAlumnos = detalleEncuesta.size();
		
		for (int i = 0; i < numeroBarras; i++) {
			for (int j = 0; j < numeroAlumnos; j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				log.info("fin");
				if(i == 0) { //DA
					dataset.addValue(sumSintomas.get("sumDa"), "Deficit de Atencion", "N"+(j+1));
				} else if (i == 1) { //H
					dataset.addValue(sumSintomas.get("sumH"), "Hiperactividad", "N"+(j+1));
				} else {
					dataset.addValue(sumSintomas.get("sumI"), "Impulsividad", "N"+(j+1));
				}
				
			}
			
		}
		
		exportPdf(dataset, 1);
		
	}
	

	private void generateReports() {
		final String fiat = "HIPERACTIVIDAD";
		final String audi = "DEFICIT DE ATENCION";
		final String ford = "IMPULSIVIDAD";
		
		
		final String speed = "N1";
		final String millage = "N2";
		final String userrating = "N3";
		final String safety = "N4";

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		dataset.addValue(1.0, fiat, speed);
		dataset.addValue(3.0, fiat, userrating);
		dataset.addValue(5.0, fiat, millage);
		dataset.addValue(5.0, fiat, safety);

		dataset.addValue(5.0, audi, speed);
		dataset.addValue(6.0, audi, userrating);
		dataset.addValue(10.0, audi, millage);
		dataset.addValue(4.0, audi, safety);

		dataset.addValue(4.0, ford, speed);
		dataset.addValue(2.0, ford, userrating);
		dataset.addValue(3.0, ford, millage);
		dataset.addValue(6.0, ford, safety);

		JFreeChart barChart = ChartFactory.createBarChart("PROMEDIO SINTOMAS TDAH PRIMER GRADO", "Sintomas", "Promedio", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setTitle("Reportes Sistema TDAH");
		pdfDoc.setAuthor("Abel Kevin Nuñez Chavez");

		Page page = pdfDoc.createPage(new Rectangle(612, 468));
		PDFGraphics2D g2 = page.getGraphics2D();

		barChart.draw(g2, new Rectangle(0, 0, 612, 468));

		pdfDoc.writeToFile(new File("E:\\barchart.pdf"));
	}
	
	

}
