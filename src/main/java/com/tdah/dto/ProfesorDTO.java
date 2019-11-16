package com.tdah.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfesorDTO {
	private Integer codPersona;
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String genero;
	private String tipoDocumento;
	private String numeroDocumento;
	private Date fechaNacimiento;
	private String codigoDocente;
}
