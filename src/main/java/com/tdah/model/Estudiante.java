package com.tdah.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Estudiante extends Persona implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String tipoFamilia;
	private String estado;
	
	@JsonIgnoreProperties(value={"estudiante","hibernateLazyInitializer","handler"}, allowSetters=true)
	@OneToMany(fetch=FetchType.EAGER, mappedBy="estudiante")
	List<DetalleEncuesta> detalleEncuestas;
	
	@JsonIgnoreProperties(value={"estudiante","hibernateLazyInitializer","handler"}, allowSetters=true)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="cod_estudiante")
	private List<Contacto> contactos;
	
	public Estudiante() {
		this.detalleEncuestas = new ArrayList<DetalleEncuesta>();
		this.contactos = new ArrayList<Contacto>();
	}
	
	


}
