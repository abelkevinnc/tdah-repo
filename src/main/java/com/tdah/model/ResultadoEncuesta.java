package com.tdah.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoEncuesta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codResultadoEncuesta;
	
	private int numeroItem;
	private String respuestaItem;
	
	@Transient
	private String descripcionItem;

	public ResultadoEncuesta(int numeroItem, String descripcionItem) {
		super();
		this.numeroItem = numeroItem;
		this.descripcionItem = descripcionItem;
	}

	
	
}
