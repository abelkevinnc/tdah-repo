package com.tdah.service;

import java.util.List;

import com.tdah.dto.EstudianteDTO;
import com.tdah.model.Estudiante;

public interface IEstudianteService  extends ICRUD<Estudiante>{
	List<EstudianteDTO> listarEstudiantesFront();
}
