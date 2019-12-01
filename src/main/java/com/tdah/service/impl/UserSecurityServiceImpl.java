package com.tdah.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tdah.model.Usuario;
import com.tdah.service.IUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserSecurityServiceImpl implements UserDetailsService{
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		
		log.info("Autenticación para el usuario: " + nombreUsuario);
		
		Usuario usuario = usuarioService.getUserByUsername(nombreUsuario);
		
		if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }
		
		//String claveEncriptada = "$2a$10$KhTn69Tlt68luXvVs3qZhOOrO7TMTXViBamNex/Ub4A7NKkiaSd7u";

		//ROL POR DEFECTO
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_DIRECTOR"));
		UserDetails userDetails = new User(nombreUsuario, usuario.getClave(), roles);
		
		return userDetails;
	}

}
