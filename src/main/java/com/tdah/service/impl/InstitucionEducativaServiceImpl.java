package com.tdah.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IInstitucionEducativaDAO;
import com.tdah.model.InstitucionEducativa;
import com.tdah.service.IInstitucionEducativaService;

@Service
public class InstitucionEducativaServiceImpl implements IInstitucionEducativaService{

	@Autowired
	IInstitucionEducativaDAO dao;
	
	@Override
	public List<InstitucionEducativa> findAll() {
		return dao.findAll();
	}

	@Override
	public InstitucionEducativa findById(int id) {
		return dao.findById(id).get();
	}

}
