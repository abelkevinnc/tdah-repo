package com.tdah.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdah.dao.IProfesorDAO;
import com.tdah.dto.ProfesorDTO;
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

	@Override
	public List<ProfesorDTO> listarProfesoresFront() {
		List<ProfesorDTO> profesores = new ArrayList<ProfesorDTO>();
		findAll()
		.stream()
		.forEach(p -> {
			ProfesorDTO prof = ProfesorDTO.builder()
					.codPersona(p.getCodPersona())
					.primerNombre(p.getPrimerNombre())
					.segundoNombre(p.getSegundoNombre())
					.apellidoPaterno(p.getApellidoPaterno())
					.apellidoMaterno(p.getApellidoMaterno())
					.genero(p.getGenero())
					.tipoDocumento(p.getTipoDocumento())
					.numeroDocumento(p.getNumeroDocumento())
					.fechaNacimiento(p.getFechaNacimiento())
					.codigoDocente(p.getCodigoDocente())
					.build();
			profesores.add(prof);
		});
		
		return profesores;
	}

}