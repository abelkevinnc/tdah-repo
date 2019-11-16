package com.tdah.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IUsuarioDAO;
import com.tdah.model.Usuario;
import com.tdah.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	@Autowired
	IUsuarioDAO dao;

	@Override
	public List<Usuario> findAll() {

		return dao.findAll();
	}

	@Override
	public Usuario saveOrUpdate(Usuario obj) {

		return dao.save(obj);
	}

	@Override
	public Usuario findById(int id) {

		return dao.findById(id).get();
	}

	@Override
	public void delete(int id) {
		dao.deleteById(id);

	}

	@Override
	public Usuario usuarioLogin(String nombreUsuario, String clave) {
		return dao.usuarioLogin(nombreUsuario, clave);
	}

}
