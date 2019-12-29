package com.tdah.restcontroller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tdah.model.Reporte;
import com.tdah.service.IReporteService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
@Slf4j
public class ReporteRestController {
	
	@Autowired
	IReporteService reporteService;
	
	@GetMapping("/encuestas/ver-pdf/{codEncuesta}")
	public void verEncuestaPdf(@PathVariable(value = "codEncuesta") Integer codEncuesta, HttpServletResponse response) {
		log.info("Reporte Rest Controller: Reporte"+ codEncuesta);
		Reporte reporte = reporteService.findById(codEncuesta);
		InputStream ie = null;
		try {
			ie = getArchivo(reporte.getDenominacionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(ie != null) {
			try {
				byte[] data = getArrayFromInputStream(ie);
				streamReport(response, data, reporte.getDenominacionArchivo());
			} catch (Exception e) {
				e.printStackTrace();
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
	
	public InputStream getArchivo(String titulo_archivo) {

		InputStream inputStream = null;
		FTPClient cliente = new FTPClient();
		if (conectar(cliente, "localhost", "ftpserver", "123456")) {
			try {
				cliente.changeWorkingDirectory("/");
				inputStream = cliente.retrieveFileStream(titulo_archivo);
				System.out.println(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return inputStream;
	}
	
	
	public boolean conectar(FTPClient cliente, String server, String user, String password) {
		try {
			// Conectarse e identificarse.
			cliente.connect(server, 21);
			if (cliente.login(user, password)) {
				// Entrando a modo pasivo
				cliente.enterLocalPassiveMode();
				// Activar recibir/enviar cualquier tipo de archivo
				cliente.setFileType(FTP.BINARY_FILE_TYPE);

				// Obtener respuesta del servidor y acceder.
				int respuesta = cliente.getReplyCode();
				if (FTPReply.isPositiveCompletion(respuesta) == true) {
					System.out.println(cliente.printWorkingDirectory());
					return true;
				} else {
					return false;
				}
			} else {
				System.err.println("Usuario o contraseña incorrectos.");
				return false;
			}

		} catch (IOException e) {
			System.err.println("Host del servidor incorrecto: " + server);
			e.printStackTrace();
			return false;
		}
	}
	
	public byte[] getArrayFromInputStream(InputStream inputStream) throws IOException {
		byte[] bytes;
	    byte[] buffer = new byte[1024];
	    try(BufferedInputStream is = new BufferedInputStream(inputStream)){
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        int length;
	        while ((length = is.read(buffer)) > -1 ) {
	            bos.write(buffer, 0, length);
	        }
	        bos.flush();
	        bytes = bos.toByteArray();
	        //System.out.println("convert to byte success!");
	    }
	    
	    return bytes;
	}

}
