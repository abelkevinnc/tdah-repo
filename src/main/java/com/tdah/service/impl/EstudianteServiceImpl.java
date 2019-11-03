package com.tdah.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IEstudianteDAO;
import com.tdah.model.Estudiante;
import com.tdah.service.IEstudianteService;
@Service
public class EstudianteServiceImpl implements IEstudianteService{
	@Autowired
	IEstudianteDAO dao;
	
	@Override
	public List<Estudiante> findAll() {
		
		return dao.findAll();
	}

	@Override
	public Estudiante saveOrUpdate(Estudiante obj) {
		
		return dao.save(obj);
	}

	@Override
	public Estudiante findById(int id) {
		
		return dao.findById(id).orElse(null);
	}

	@Override
	public void delete(int id) {
		dao.deleteById(id);
		
	}

}