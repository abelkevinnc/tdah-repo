package com.tdah.service.impl;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.orsonpdf.PDFDocument;
import com.orsonpdf.PDFGraphics2D;
import com.orsonpdf.Page;
import com.tdah.dao.IReporteDAO;
import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.model.Reporte;
import com.tdah.model.ResultadoEncuesta;
import com.tdah.service.IEncuestaService;
import com.tdah.service.IEstudianteService;
import com.tdah.service.IReporteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReporteServiceImpl implements IReporteService{

	@Autowired
	IEncuestaService encuestaService;
	
	@Autowired
	IEstudianteService estudianteService;
	
	@Autowired
	IReporteDAO reporteDAO;
	
	@Override
	public void generarReporteSintomasPorGrado(Integer codEncuesta) {

		Encuesta encuesta = encuestaService.findById(codEncuesta);
		List<DetalleEncuesta> detalleEncuestas = encuesta.getDetalleEncuestas();
		Map<String, List<DetalleEncuesta>> encuentasXgrado = dividirEncuestasPorGrado(detalleEncuestas);
		Map<String,List<Double>> promedios = new HashMap<String, List<Double>>();
		
		List<Double> promedioDa = new ArrayList<Double>();
		List<Double> promedioH = new ArrayList<Double>();
		List<Double> promedioI = new ArrayList<Double>();
		for (int i = 0; i < 6; i++) {
			List<DetalleEncuesta> detalleEncuesta = encuentasXgrado.get("encuesta"+(i+1));
			Double sumPuntajeda = 0.0;
			Double sumPuntajeh = 0.0;
			Double sumPuntajei = 0.0;
			for (int j = 0; j < detalleEncuesta.size(); j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				
				sumPuntajeda += sumSintomas.get("sumDA");
				sumPuntajeh += sumSintomas.get("sumH");
				sumPuntajei += sumSintomas.get("sumI");
			}
			//retorna el promedio de todo el grado
			
			promedioDa.add(sumPuntajeda/detalleEncuesta.size());
			promedioH.add(sumPuntajeh/detalleEncuesta.size());
			promedioI.add(sumPuntajei/detalleEncuesta.size());
			
		}
		
		promedios.put("promedioDA", promedioDa);
		promedios.put("promedioH", promedioH);
		promedios.put("promedioI", promedioI);
		
		//numero de grados
		int numeroGrados = 6;
		
		for (int i = 0; i < 3; i++) {
			final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			Map<String, String> utilReporteSintomasPorGrado = getUtilReporteSintomasPorGrado(i, codEncuesta);
			for (int j = 0; j < numeroGrados; j++) {
				List<Double> prom = promedios.get("promedio"+utilReporteSintomasPorGrado.get("abreviacion"));
				dataset.addValue(prom.get(j), utilReporteSintomasPorGrado.get("sintoma"), (j+1)+"G");	
			}
			exportPdf(dataset, utilReporteSintomasPorGrado);
			//guardar nombre de los reportes en la bd
			Reporte reporte = new Reporte();
			reporte.setCodEncuesta(codEncuesta);
			reporte.setDenominacionArchivo(utilReporteSintomasPorGrado.get("denominacion_archivo")+".pdf");
			reporte.setCodOrden(1);
			reporteDAO.save(reporte);
		}
			
		
		
		
		
		
	}
	
	@Override
	public void generarReporteSintomasIndividualesPorGrado() {
		Encuesta encuesta = encuestaService.findAll().get(0);
		List<DetalleEncuesta> detalleEncuestas = encuesta.getDetalleEncuestas();
		
		
		
		//objetivo especifico 
		//DA; según grado
		//H; según grado
		//I; según grado
		Map<String, List<DetalleEncuesta>> encuentas = dividirEncuestasPorGrado(detalleEncuestas);
		
		for (int i = 1; i <= 6; i++) {
			log.info("encuesta"+i);
			List<DetalleEncuesta> detalleEncuesta = encuentas.get("encuesta"+i);
			for (int j = 0; j < 3; j++) {
				reportSumSintomas(detalleEncuesta, getSintoma(i,j)); //reporte sum sintomas
			}
		}
		
	}
	
	private Map<String, String> getUtilReporteSintomasPorGenero(int num, int codEncuesta) {
		Map<String, String> utilReporteSintomasPorGenero = new HashMap<>();
		switch (num) {
		case 0:
			utilReporteSintomasPorGenero.put("abreviacion", "DA");
			utilReporteSintomasPorGenero.put("sintoma", "Deficit de atencion");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte promedio deficit de atencion por genero");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_DA_GENERO_"+codEncuesta);
			
			break;
		case 1:
			utilReporteSintomasPorGenero.put("abreviacion", "H");
			utilReporteSintomasPorGenero.put("sintoma", "Hiperactividad");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte promedio hiperactividad por genero");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_H_GENERO_"+codEncuesta);
			break;
		case 2:
			utilReporteSintomasPorGenero.put("abreviacion", "I");
			utilReporteSintomasPorGenero.put("sintoma", "Impulsividad");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte promedio impulsividad por genero");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_I_GENERO_"+codEncuesta);
			break;
		default:
			break;
		}
		
		return utilReporteSintomasPorGenero;
		
	}
	
	private Map<String, String> getUtilReporteSintomasPorGrado(int num, int codEncuesta) {
		Map<String, String> utilReporteSintomasPorGrado = new HashMap<>();
		
		switch (num) {
		case 0:
			utilReporteSintomasPorGrado.put("abreviacion", "DA");
			utilReporteSintomasPorGrado.put("sintoma", "Deficit de atencion");
			utilReporteSintomasPorGrado.put("titulo_pdf", "Reporte promedio deficit de atencion");
			utilReporteSintomasPorGrado.put("denominacion_archivo", "REPORTE_PROMEDIO_DA_GRADO_"+codEncuesta);
			
			break;
		case 1:
			utilReporteSintomasPorGrado.put("abreviacion", "H");
			utilReporteSintomasPorGrado.put("sintoma", "Hiperactividad");
			utilReporteSintomasPorGrado.put("titulo_pdf", "Reporte promedio hiperactividad");
			utilReporteSintomasPorGrado.put("denominacion_archivo", "REPORTE_PROMEDIO_H_GRADO_"+codEncuesta);
			break;
		case 2:
			utilReporteSintomasPorGrado.put("abreviacion", "I");
			utilReporteSintomasPorGrado.put("sintoma", "Impulsividad");
			utilReporteSintomasPorGrado.put("titulo_pdf", "Reporte promedio impulsividad");
			utilReporteSintomasPorGrado.put("denominacion_archivo", "REPORTE_PROMEDIO_I_GRADO_"+codEncuesta);
			break;
		default:
			break;
		}
		
		return utilReporteSintomasPorGrado;
	}
	
	private Map<String, String> getUtilReporteSintomasPorTF(int num, int codEncuesta) {
		Map<String, String> utilReporteSintomasPorGenero = new HashMap<>();
		switch (num) {
		case 0:
			utilReporteSintomasPorGenero.put("abreviacion", "DA");
			utilReporteSintomasPorGenero.put("sintoma", "Deficit de atencion");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte deficit de atencion por tipo de familia (promedio)");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_DA_TF_"+codEncuesta);
			
			break;
		case 1:
			utilReporteSintomasPorGenero.put("abreviacion", "H");
			utilReporteSintomasPorGenero.put("sintoma", "Hiperactividad");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte hiperactividad por tipo de familia (promedio)");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_H_TF_"+codEncuesta);
			break;
		case 2:
			utilReporteSintomasPorGenero.put("abreviacion", "I");
			utilReporteSintomasPorGenero.put("sintoma", "Impulsividad");
			utilReporteSintomasPorGenero.put("titulo_pdf", "Reporte impulsividad por tipo de familia (promedio)");
			utilReporteSintomasPorGenero.put("denominacion_archivo", "REPORTE_PROMEDIO_I_TF_"+codEncuesta);
			break;
		default:
			break;
		}
		
		return utilReporteSintomasPorGenero;
		
	}

	private Map<String, String> getSintoma(int grado, int num) {
		
		Map<String, String> utilSintoma = new HashMap<>();
		
		switch (num) {
		case 0:
			utilSintoma.put("abreviacion", "DA");
			utilSintoma.put("sintoma", "Deficit de atencion");
			utilSintoma.put("titulo_pdf", "Reporte Deficit de atencion "+grado+ " grado");
			utilSintoma.put("denominacion_archivo", "REPORTE_DA_GRADO_"+grado);
			
			break;
		case 1:
			utilSintoma.put("abreviacion", "H");
			utilSintoma.put("sintoma", "Hiperactividad");
			utilSintoma.put("titulo_pdf", "Reporte Hiperactividad "+grado+ " grado");
			utilSintoma.put("denominacion_archivo", "REPORTE_H_GRADO_"+grado);
			break;
		case 2:
			utilSintoma.put("abreviacion", "I");
			utilSintoma.put("sintoma", "Impulsividad");
			utilSintoma.put("titulo_pdf", "Reporte Impulsividad "+grado+ " grado");
			utilSintoma.put("denominacion_archivo", "REPORTE_I_GRADO_"+grado);
			break;
		default:
			break;
		}
		return utilSintoma;
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
	
	
	private void reportSumSintomas(List<DetalleEncuesta> detalleEncuesta, Map<String, String> utilSintoma) { //todos los grados
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//numero de barras
		int numeroBarras = 1;
		
		//numero de alumnos
		int numeroAlumnos = detalleEncuesta.size();
		
		for (int i = 0; i < numeroBarras; i++) {
			for (int j = 0; j < numeroAlumnos; j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				dataset.addValue(sumSintomas.get("sum"+utilSintoma.get("abreviacion")), utilSintoma.get("sintoma"), "N"+(j+1));	
			}
		}
		exportPdf(dataset, utilSintoma);
		
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
		
		sumSintomas.put("sumDA",sumDa);
		sumSintomas.put("sumH",sumH);
		sumSintomas.put("sumI",sumI);
		
		return sumSintomas;
	}
	
	public void exportPdf(DefaultCategoryDataset dataset, Map<String, String> utilSintoma) {
		JFreeChart barChart = ChartFactory.createBarChart(utilSintoma.get("titulo_pdf"), "Sintomas", "Puntaje", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		/* Get instance of CategoryPlot */
		CategoryPlot plot = barChart.getCategoryPlot();

		/* Change Bar colors */
		BarRenderer renderer = (BarRenderer) plot.getRenderer();

		renderer.setSeriesPaint(0, Color.BLUE);

		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0);
		
		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setTitle("Reportes Sistema TDAH");
		pdfDoc.setAuthor("Abel Kevin Nuñez Chavez");

		Page page = pdfDoc.createPage(new Rectangle(612, 468));
		PDFGraphics2D g2 = page.getGraphics2D();

		barChart.draw(g2, new Rectangle(18, 20, 562, 418));
		
		
		String denominacionArchivo = utilSintoma.get("denominacion_archivo"); 
		
		//crear una carpeta temporal
		String pathTemp = "pathTemp";
		
		File directorio = new File(pathTemp);
		if(directorio.exists()) {
			//log.info("El directorio ya existe.");
		} else {
			if(directorio.mkdir()) {
				log.info("carpeta creada con nombre: "+ pathTemp);
			}
		}
		
		
		pdfDoc.writeToFile(new File(pathTemp + "\\" + denominacionArchivo + ".pdf"));
		
		//enviar a azure
		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=abelazure1;"
				+ "AccountKey=Xv/27PedgNDVoUwjHPkuuy4BeTS/ZLM2yvvBowuL9WO9vQBhVI/3XEBlB+8wyG1tYEmHzIshvOjYgyyH+ZcZSg==";
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			CloudFileClient fileClient = storageAccount.createCloudFileClient();
			CloudFileShare share = fileClient.getShareReference("reportes");
			CloudFileDirectory rootDir = share.getRootDirectoryReference();
			
			// Define the path to a local file.
		    final String filePath = pathTemp + "\\" + denominacionArchivo + ".pdf";
			
			CloudFile cloudFile = rootDir.getFileReference(denominacionArchivo + ".pdf");
		    cloudFile.uploadFromFile(filePath);
			
		} catch (InvalidKeyException | URISyntaxException | StorageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Reporte> findAll() {
		return reporteDAO.findAll();
	}

	@Override
	public Reporte saveOrUpdate(Reporte obj) {

		return reporteDAO.save(obj);
	}

	@Override
	public Reporte findById(int id) {
		return reporteDAO.findById(id).get();
	}

	@Override
	public void delete(int id) {
		reporteDAO.deleteById(id);
		
	}

	@Override
	public List<Reporte> getByEncuestaId(Integer codEncuesta) {
		return reporteDAO.getByEncuestaId(codEncuesta);
	}

	@Override
	public void generarReporteSintomasPorGenero(Integer codEncuesta) {
		Encuesta encuesta = encuestaService.findById(codEncuesta);
		List<DetalleEncuesta> detalleEncuestas = encuesta.getDetalleEncuestas();
		Map<String, List<DetalleEncuesta>> encuestasXGenero = dividirEncuestasPorGenero(detalleEncuestas);

		
		Map<String,List<Double>> promedios = new HashMap<String, List<Double>>();
		
		List<Double> promedioDa = new ArrayList<Double>();
		List<Double> promedioH = new ArrayList<Double>();
		List<Double> promedioI = new ArrayList<Double>();
		
		for (int i = 0; i < 2; i++) {
			List<DetalleEncuesta> detalleEncuesta = encuestasXGenero.get("encuesta"+(i+1));
			Double sumPuntajeda = 0.0;
			Double sumPuntajeh = 0.0;
			Double sumPuntajei = 0.0;
			for (int j = 0; j < detalleEncuesta.size(); j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				
				sumPuntajeda += sumSintomas.get("sumDA");
				sumPuntajeh += sumSintomas.get("sumH");
				sumPuntajei += sumSintomas.get("sumI");
			}
			//retorna el promedio de todo el grupo encuestado
			
			promedioDa.add(sumPuntajeda/detalleEncuesta.size());
			promedioH.add(sumPuntajeh/detalleEncuesta.size());
			promedioI.add(sumPuntajei/detalleEncuesta.size());
			
		}
		
		promedios.put("promedioDA", promedioDa);
		promedios.put("promedioH", promedioH);
		promedios.put("promedioI", promedioI);
		
		//numero de genero
				int numeroGenero = 2;
				
				for (int i = 0; i < 3; i++) {
					final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					Map<String, String> utilReporteSintomasPorGenero = getUtilReporteSintomasPorGenero(i, codEncuesta);
					for (int j = 0; j < numeroGenero; j++) {
						List<Double> prom = promedios.get("promedio"+utilReporteSintomasPorGenero.get("abreviacion"));
						if(j==0) dataset.addValue(prom.get(j), utilReporteSintomasPorGenero.get("sintoma"), "M");	
						else dataset.addValue(prom.get(j), utilReporteSintomasPorGenero.get("sintoma"), "F");	
					}
					exportPdf(dataset, utilReporteSintomasPorGenero);
					//guardar nombre de los reportes en la bd
					Reporte reporte = new Reporte();
					reporte.setCodEncuesta(codEncuesta);
					reporte.setDenominacionArchivo(utilReporteSintomasPorGenero.get("denominacion_archivo")+".pdf");
					reporte.setCodOrden(2);
					reporteDAO.save(reporte);
				}
		
	}
	
	private Map<String, List<DetalleEncuesta>> dividirEncuestasPorGenero(List<DetalleEncuesta> detalleEncuestas) {
		Map<String, List<DetalleEncuesta>> encuentas = new HashMap<>();
		
		List<DetalleEncuesta> encuestadosM = detalleEncuestas
				.stream()
				.filter(de -> de.getEstudiante().getGenero().equalsIgnoreCase("M"))
				.collect(Collectors.toList());
		
		List<DetalleEncuesta> encuestadosF = detalleEncuestas
				.stream()
				.filter(de -> de.getEstudiante().getGenero().equalsIgnoreCase("F"))
				.collect(Collectors.toList());
		
		encuentas.put("encuesta1", encuestadosM);
		encuentas.put("encuesta2", encuestadosF);
		
		return encuentas;
	}

	@Override
	public void generarReporteSintomasPorTipoFamilia(Integer codEncuesta) {
		Encuesta encuesta = encuestaService.findById(codEncuesta);
		List<DetalleEncuesta> detalleEncuestas = encuesta.getDetalleEncuestas();
		Map<String, List<DetalleEncuesta>> encuestasXTipoFamilia = dividirEncuestasPorTipoFamilia(detalleEncuestas);

		
		Map<String,List<Double>> promedios = new HashMap<String, List<Double>>();
		
		List<Double> promedioDa = new ArrayList<Double>();
		List<Double> promedioH = new ArrayList<Double>();
		List<Double> promedioI = new ArrayList<Double>();
		
		for (int i = 0; i < 3; i++) {
			List<DetalleEncuesta> detalleEncuesta = encuestasXTipoFamilia.get("encuesta"+(i+1));
			Double sumPuntajeda = 0.0;
			Double sumPuntajeh = 0.0;
			Double sumPuntajei = 0.0;
			for (int j = 0; j < detalleEncuesta.size(); j++) {
				List<ResultadoEncuesta> resultadoEncuestas = detalleEncuesta.get(j).getResultadoEncuestas();
				Map<String, Integer> sumSintomas = sintomasSeleccionadasPorAlumno(resultadoEncuestas);
				
				sumPuntajeda += sumSintomas.get("sumDA");
				sumPuntajeh += sumSintomas.get("sumH");
				sumPuntajei += sumSintomas.get("sumI");
			}
			//retorna el promedio de todo el grupo encuestado separados por tipo de familia
			
			promedioDa.add(sumPuntajeda/detalleEncuesta.size());
			promedioH.add(sumPuntajeh/detalleEncuesta.size());
			promedioI.add(sumPuntajei/detalleEncuesta.size());
			
		}
		
		promedios.put("promedioDA", promedioDa);
		promedios.put("promedioH", promedioH);
		promedios.put("promedioI", promedioI);
		
		//numero de tipos de familia
				int numeroTF = 3;
				
				for (int i = 0; i < 3; i++) {
					final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					Map<String, String> utilReporteSintomasPorTF = getUtilReporteSintomasPorTF(i, codEncuesta);
					for (int j = 0; j < numeroTF; j++) {
						List<Double> prom = promedios.get("promedio"+utilReporteSintomasPorTF.get("abreviacion"));
						if(j==0) {
							dataset.addValue(prom.get(j), utilReporteSintomasPorTF.get("sintoma"), "TF A");	
						}
						else if(j==1) {
							dataset.addValue(prom.get(j), utilReporteSintomasPorTF.get("sintoma"), "TF B");	
						}
						else if(j==2) {
							dataset.addValue(prom.get(j), utilReporteSintomasPorTF.get("sintoma"), "TF C");	
						}
					}
					exportPdf(dataset, utilReporteSintomasPorTF);
					//guardar nombre de los reportes en la bd
					Reporte reporte = new Reporte();
					reporte.setCodEncuesta(codEncuesta);
					reporte.setDenominacionArchivo(utilReporteSintomasPorTF.get("denominacion_archivo")+".pdf");
					reporte.setCodOrden(3);
					reporteDAO.save(reporte);
				}
		
	}
	
	private Map<String, List<DetalleEncuesta>> dividirEncuestasPorTipoFamilia(List<DetalleEncuesta> detalleEncuestas) {
		Map<String, List<DetalleEncuesta>> encuentas = new HashMap<>();
		
		List<DetalleEncuesta> encuestadosA = detalleEncuestas
				.stream()
				.filter(de -> de.getEstudiante().getTipoFamilia().equalsIgnoreCase("A"))
				.collect(Collectors.toList());
		
		List<DetalleEncuesta> encuestadosB = detalleEncuestas
				.stream()
				.filter(de -> de.getEstudiante().getTipoFamilia().equalsIgnoreCase("B"))
				.collect(Collectors.toList());
		
		List<DetalleEncuesta> encuestadosC = detalleEncuestas
				.stream()
				.filter(de -> de.getEstudiante().getTipoFamilia().equalsIgnoreCase("C"))
				.collect(Collectors.toList());
		
		encuentas.put("encuesta1", encuestadosA);
		encuentas.put("encuesta2", encuestadosB);
		encuentas.put("encuesta3", encuestadosC);
		
		return encuentas;
	}

	
}
