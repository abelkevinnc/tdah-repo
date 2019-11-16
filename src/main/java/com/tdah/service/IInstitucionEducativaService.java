package com.tdah.service;

import java.util.List;

import com.tdah.model.InstitucionEducativa;

public interface IInstitucionEducativaService {
	List<InstitucionEducativa> findAll();
	InstitucionEducativa findById(int id);
}
