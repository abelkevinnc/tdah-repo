package com.tdah;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class AppTdahApplicationTests {
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Test
	void contextLoads() {
		String passwordLine = "123";
		
		String passwordEncriptado = encoder.encode(passwordLine);
		
		System.out.println(passwordEncriptado);
	}
	
	

}
