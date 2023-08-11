package com.pe.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.pe.app.documents.Pais;

import reactor.core.publisher.Mono;

public interface PaisRepository extends ReactiveMongoRepository<Pais, String>{
	
	public Mono<Pais> findByNombre(String nombre);

}
