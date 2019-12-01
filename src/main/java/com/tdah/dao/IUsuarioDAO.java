package com.tdah.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tdah.model.Usuario;

public interface IUsuarioDAO extends JpaRepository<Usuario, Integer>{
	
	@Query(value="select * from usuario where nombre_usuario =?1 and clave =?2", nativeQuery=true)
	Usuario usuarioLogin(String nombreUsuario, String clave);
	
	@Query(value = "select * from usuario where nombre_usuario=?1", nativeQuery=true)
	Usuario getUserByUsername(String nombreUsuario);
}
