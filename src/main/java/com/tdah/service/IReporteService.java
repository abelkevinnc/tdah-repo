package com.tdah.service;

import java.util.List;

import com.tdah.model.Reporte;

public interface IReporteService extends ICRUD<Reporte> {
	void generarReporteSintomasPorGrado(Integer codEncuesta);
	void generarReporteSintomasIndividualesPorGrado();
	List<Reporte> getByEncuestaId(Integer codEncuesta);
};
