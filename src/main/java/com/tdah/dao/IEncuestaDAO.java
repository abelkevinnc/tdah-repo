package com.tdah.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;

public interface IEncuestaDAO extends JpaRepository<Encuesta, Integer>{
	
	@Query(value="from DetalleEncuesta d")
	List<DetalleEncuesta> getDetalleEncuestas();

}
