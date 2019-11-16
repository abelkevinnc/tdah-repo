package com.tdah.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DetalleEncuesta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codDetalleencuesta;
	
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="cod_estudiante")
//	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	
	@JsonIgnoreProperties(value={"detalleEncuestas","hibernateLazyInitializer","handler"}, allowSetters=true)
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="cod_estudiante")
	private Estudiante estudiante;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_profesor")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Profesor profesor;
	
	private String nivelEducacion;
	private String gradoEstudio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaAplicacion;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="cod_detalle_encuesta")
	private List<ResultadoEncuesta> resultadoEncuestas;
	
	
}
