package com.tdah.dto;

import java.util.Date;

import com.tdah.model.Rol;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {
	private Integer codUsuario;
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String genero;
	private String tipoDocumento;
	private String numeroDocumento;
	private Date fechaNacimiento;
	
	private Rol rol;
	private Date fechaRegistro;
	
	private String nombreUsuario;
	private String clave;
	
	

}
