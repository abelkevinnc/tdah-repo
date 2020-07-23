package com.tdah.service.impl;

import com.tdah.service.IFtpService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FtpServiceImpl implements IFtpService {

    private static String server = "localhost";
    private static String user = "ftpserver";
    private static String password = "server123456";

    @Override
    public InputStream getArchivo(String titulo_archivo) {
        InputStream inputStream = null;
        FTPClient cliente = new FTPClient();
        if (conectar(cliente)) {
            try {
                cliente.changeWorkingDirectory("/");
                inputStream = cliente.retrieveFileStream(titulo_archivo);
                System.out.println(inputStream);
                cliente.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inputStream;
    }

    @Override
    public boolean conectar(FTPClient cliente) {
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

    @Override
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
        }

        return bytes;
    }
}
