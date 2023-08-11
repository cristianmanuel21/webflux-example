package com.pe.app.documents;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="personas")
public class Persona {
	
	@Id
	private String id;
	@NotBlank
	private String dni;
	@NotBlank
	private String nombre;
	@NotBlank
	private String apellido;
	@PositiveOrZero
	private int edad;
	@NotNull
	private Pais pais;
	
	public Persona(String dni,String nombre,String apellido,int edad,Pais pais) {
		super();
		this.dni=dni;
		this.nombre=nombre;
		this.apellido=apellido;
		this.edad=edad;
		this.pais=pais;
	} 
	
}
