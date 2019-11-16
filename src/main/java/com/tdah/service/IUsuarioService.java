package com.tdah.service;

import com.tdah.model.Usuario;

public interface IUsuarioService extends ICRUD<Usuario>{
	Usuario usuarioLogin(String nombreUsuario, String clave);
}
