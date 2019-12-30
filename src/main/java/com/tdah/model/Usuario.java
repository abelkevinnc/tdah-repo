package com.tdah.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codUsuario;
	private String nombreUsuario;
	private String clave;
	
	private Date fechaRegistro;
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rol_id")
    private Rol rol;
	
	//datos personales
	
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String genero;
	private String tipoDocumento;
	private String numeroDocumento;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaNacimiento;
	
	
	@JsonIgnoreProperties(value={"usuario","hibernateLazyInitializer","handler"}, allowSetters=true)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="cod_usuario")
	private List<Contacto> contactos;
	
	public Usuario() {
		this.contactos = new ArrayList<Contacto>();
	}
	
}
