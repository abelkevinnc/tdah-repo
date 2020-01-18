package com.tdah;

import java.io.File;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FtpTest {
	@Test
	void contextLoads() {
		String pathRandom = UUID.randomUUID().toString();
		
		File directorio = new File(pathRandom);
		if(directorio.mkdir()) {
			log.info("carpeta creada con nombre: "+ pathRandom);
			
			
		}
	}
}
