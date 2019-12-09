package com.tdah.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tdah.model.Reporte;

public interface IReporteDAO extends JpaRepository<Reporte, Integer> {
	@Query(value = "select * from reporte where cod_encuesta=?1", nativeQuery=true)
	List<Reporte> getByEncuestaId(Integer codEncuesta);
}
