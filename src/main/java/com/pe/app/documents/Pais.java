package com.pe.app.documents;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="paises")
public class Pais {
	
	@Id
	private String id;
	@NotBlank
	private String nombre;
	public Pais(String nombre) {
		super();
		this.nombre=nombre;
	}
}
