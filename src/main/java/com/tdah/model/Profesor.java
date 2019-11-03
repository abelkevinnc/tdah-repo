package com.tdah.model;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profesor extends Persona implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	String codigoDocente;

}
