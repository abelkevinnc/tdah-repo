package com.tdah.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IEstudianteDAO;
import com.tdah.dto.EstudianteDTO;
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

	@Override
	public List<EstudianteDTO> listarEstudiantesFront() {
		
		List<EstudianteDTO> estudiantes = new ArrayList<EstudianteDTO>();
		findAll()
		.stream()
		.forEach(e -> {
			
			EstudianteDTO est = EstudianteDTO.builder()
					.codPersona(e.getCodPersona())
					.primerNombre(e.getPrimerNombre())
					.segundoNombre(e.getSegundoNombre())
					.apellidoMaterno(e.getApellidoMaterno())
					.apellidoPaterno(e.getApellidoPaterno())
					.genero(e.getGenero())
					.tipoDocumento(e.getTipoDocumento())
					.numeroDocumento(e.getNumeroDocumento())
					.fechaNacimiento(e.getFechaNacimiento())
					.tipoFamilia(e.getTipoFamilia())
					.build();
			
			estudiantes.add(est);
		});
		return estudiantes;
	}

}