package com.tdah.service;

import java.util.List;

import com.tdah.model.DetalleEncuesta;
import com.tdah.model.Encuesta;

public interface IEncuestaService extends ICRUD<Encuesta> {
	List<DetalleEncuesta> getDetalleEncuestas();
}
