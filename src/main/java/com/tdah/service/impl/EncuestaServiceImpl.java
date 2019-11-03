package com.tdah.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IEncuestaDAO;
import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;
import com.tdah.service.IEncuestaService;
@Service
public class EncuestaServiceImpl implements IEncuestaService{
	@Autowired
	IEncuestaDAO dao;
	
	@Override
	public List<Encuesta> findAll() {
		
		return dao.findAll();
	}

	@Override
	public Encuesta saveOrUpdate(Encuesta obj) {
		
		return dao.save(obj);
	}

	@Override
	public Encuesta findById(int id) {
		
		return dao.findById(id).orElse(null);
	}

	@Override
	public void delete(int id) {
		dao.deleteById(id);
		
	}

	@Override
	public List<DetalleEncuesta> getDetalleEncuestas() {
		return dao.getDetalleEncuestas();
	}

}