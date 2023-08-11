package com.pe.app.service;

import com.pe.app.documents.Persona;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaService {
	
	public Flux<Persona> findAllPersona();
	public Mono<Persona> findById(String id);
	public Mono<Persona> findByDNI(String dni);
	public Mono<Persona> save(Persona persona);
	public Mono<Persona> update(Persona persona);
	public Mono<Void> delete(Persona persona);
}
