package com.tdah.service;

import java.util.List;

import com.tdah.model.Reporte;

public interface IReporteService extends ICRUD<Reporte> {
	void generarReporteSintomasPorGrado(Integer codEncuesta);
	void generarReporteSintomasIndividualesPorGrado(Integer codEncuesta);
	
	void generarReporteSintomasIndividualesPorGenero(Integer codEncuesta);
	void generarReporteSintomasPorGenero(Integer codEncuesta);
	
	void generarReporteSintomasIndividualesTF(Integer codEncuesta);
	void generarReporteSintomasPorTipoFamilia(Integer codEncuesta);
	List<Reporte> getByEncuestaId(Integer codEncuesta);
};
