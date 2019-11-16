package com.tdah.service;

import java.util.List;

import com.tdah.dto.ProfesorDTO;
import com.tdah.model.Profesor;

public interface IProfesorService extends ICRUD<Profesor> {
	public List<ProfesorDTO> listarProfesoresFront();
}
