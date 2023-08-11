package com.pe.app.service;

import com.pe.app.documents.Pais;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PaisService {
	
		public Flux<Pais> findAllPaises();
		public Mono<Pais> findById(String id);
		public Mono<Pais> findByNombre(String nombre);
		public Mono<Void> delete(Pais pais);
		public Mono<Pais> save(Pais pais);
		
	}

