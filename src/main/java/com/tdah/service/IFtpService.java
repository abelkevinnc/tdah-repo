package com.tdah.service;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public interface IFtpService {

    public InputStream getArchivo(String titulo_archivo);
    public boolean conectar(FTPClient cliente);
    public byte[] getArrayFromInputStream(InputStream inputStream) throws IOException;
}
