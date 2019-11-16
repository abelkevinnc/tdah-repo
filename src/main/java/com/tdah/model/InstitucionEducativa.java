package com.tdah.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InstitucionEducativa  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codInstitucioneducativa;
	private String codigoModular;
	private String denominacion;
	private String nivelModalidad;
	private String gestionDependencia;
	private String direccion;
	private String lugar;
	
	
	
}
