package com.pe.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pe.app.documents.Persona;

import reactor.core.publisher.Mono;

public interface PersonaRepository extends ReactiveMongoRepository<Persona, String>{
	
	public Mono<Persona> findByDni(String dni);

}
