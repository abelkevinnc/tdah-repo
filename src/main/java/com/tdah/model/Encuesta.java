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
public class Encuesta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codEncuesta;
	private String denominacion;
	private String estado;
	
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_institucioneducativa")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private InstitucionEducativa institucionEducativa;
	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="cod_encuesta")
	private List<DetalleEncuesta> detalleEncuestas;
	
	
}
