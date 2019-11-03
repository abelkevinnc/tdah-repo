package com.tdah;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FtpTest {
	@Test
	void contextLoads() {

		// Datos para la conexion
		String server = "66.220.9.50";
		String username = "abelnc";
		String password = "8dLdLteg2N4zkwJ";

		// Cliente de conexion a FTP
		FTPClient ftp = new FTPClient();

		int respuesta, i;
		String[] lista;

		try {

			// Conectando e identificandose con el servidos
			ftp.connect(server);
			ftp.login(username, password);
			// Entrando a modo pasivo
			ftp.enterLocalPassiveMode();

			// Obteniendo respuesta del servidos
			respuesta = ftp.getReplyCode();
			System.out.println("RESPUESTA: " + respuesta);

			ftp.changeWorkingDirectory("/GroupRead");
			// Si la respuesta del servidor indica podemos pasar procedemos
			if (FTPReply.isPositiveCompletion(respuesta) == true) {
				System.out.println("LISTANDO ARCHIVOS");
				lista = ftp.listNames();

				for (i = 0; i < lista.length; i++) {

					System.out.println(lista[i]);
				}
				// Si no avisamos
			} else {
				System.out.println("ERROR DE CONEXION");
			}

			// en ambos casos terminaos sesion
			ftp.logout();
			// Y nos desconectamos
			ftp.disconnect();

			// Esta excepcion se lanza en caso de algun error durante el proceso
		} catch (IOException e) {
			System.out.println("Error de conexion");
		}
	}
	
//	@Test
//	boolean subirArchivo(String tipo_archivo, String nombre_archivo, InputStream is) {
//		boolean fichSubido = false;
//		FTPClient cliente = new FTPClient();
//		if (conectar(cliente, "localhost", "ftpserver", "123456")) {
//			try {
//				cliente.changeWorkingDirectory("/"
//						+ "");
//				fichSubido = cliente.storeFile(nombre_archivo, is);
//				is.close();
//
//				cliente.logout();
//				cliente.disconnect();
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return fichSubido;
//	}
//	
//	boolean conectar(FTPClient cliente, String server, String user, String password) {
//		try {
//			// Conectarse e identificarse.
//			cliente.connect(server, 21);
//			if (cliente.login(user, password)) {
//				// Entrando a modo pasivo
//				cliente.enterLocalPassiveMode();
//				// Activar recibir/enviar cualquier tipo de archivo
//				cliente.setFileType(FTP.BINARY_FILE_TYPE);
//
//				// Obtener respuesta del servidor y acceder.
//				int respuesta = cliente.getReplyCode();
//				if (FTPReply.isPositiveCompletion(respuesta) == true) {
//					System.out.println(cliente.printWorkingDirectory());
//					return true;
//				} else {
//					return false;
//				}
//			} else {
//				System.err.println("Usuario o contraseña incorrectos.");
//				return false;
//			}
//
//		} catch (IOException e) {
//			System.err.println("Host del servidor incorrecto: " + server);
//			e.printStackTrace();
//			return false;
//		}
//	}
}
