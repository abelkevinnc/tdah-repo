package com.tdah.restcontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.storage.file.CloudFile;
import com.tdah.model.Reporte;
import com.tdah.service.IAzureStorageService;
import com.tdah.service.IReporteService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
@Slf4j
public class ReporteRestController {
	
	@Autowired
	IReporteService reporteService;
	
	@Autowired
	IAzureStorageService azureStorageService;
	
	@GetMapping("/encuestas/ver-pdf/{codEncuesta}")
	public void verEncuestaPdf(@PathVariable(value = "codEncuesta") Integer codReporte, HttpServletResponse response) {
		log.info("Reporte Rest Controller: Reporte"+ codReporte);
		Reporte reporte = reporteService.findById(codReporte);
		CloudFile file = null;
		
		//crear una carpeta temporal y traer el fichero
		String pathTempD = "pathTempD";
		try {
			
			//aqui lo nuevo de azure
			file = azureStorageService.downloadFile(reporte.getDenominacionArchivo());
			
			File directorio = new File(pathTempD);
			if(directorio.exists()) {
				//log.info("El directorio ya existe.");
			} else {
				if(directorio.mkdir()) {
					log.info("carpeta creada con nombre: "+ pathTempD);
				}
			}
			
			pathTempD = pathTempD + "/"+ reporte.getDenominacionArchivo();
			
			file.downloadToFile(pathTempD);
			//convertir a bytes y mostrar
			
			Path path = Paths.get(pathTempD);
			byte[] bArray = Files.readAllBytes(path);
	        streamReport(response, bArray, reporte.getDenominacionArchivo());
		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			File directorio = new File(pathTempD);
			if(directorio.exists()) {
				log.info("El directorio existe.");
				File[] ficheros = directorio.listFiles();
				 
				for (int x=0;x<ficheros.length;x++) {
					ficheros[x].delete();
				}				
				 
				if (directorio.delete())
				 System.out.println("El fichero "+pathTempD+" ha sido borrado correctamente");
				else
				 System.out.println("El fichero no se ha podido borrar");
			}
		}

	}
	
	protected void streamReport(HttpServletResponse response, byte[] data, String name)
            throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + name);
        response.setContentLength(data.length);

        response.getOutputStream().write(data);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
	


}
