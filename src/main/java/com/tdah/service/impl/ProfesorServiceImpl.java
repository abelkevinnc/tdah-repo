package com.tdah.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IProfesorDAO;
import com.tdah.model.Profesor;
import com.tdah.service.IProfesorService;
@Service
public class ProfesorServiceImpl implements IProfesorService{
	@Autowired
	IProfesorDAO dao;
	
	@Override
	public List<Profesor> findAll() {
		
		return dao.findAll();
	}

	@Override
	public Profesor saveOrUpdate(Profesor obj) {
		
		return dao.save(obj);
	}

	@Override
	public Profesor findById(int id) {
		
		return dao.findById(id).orElse(null);
	}

	@Override
	public void delete(int id) {
		dao.deleteById(id);
		
	}

}