package com.pe.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.app.documents.Pais;
import com.pe.app.repository.PaisRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaisServiceImpl implements PaisService{
	
	@Autowired
	private PaisRepository paisRepository;

	@Override
	public Flux<Pais> findAllPaises() {
		return paisRepository.findAll();
	}

	@Override
	public Mono<Pais> findById(String id) {
		return paisRepository.findById(id);
	}

	@Override
	public Mono<Pais> findByNombre(String nombre) {
		return paisRepository.findByNombre(nombre);
	}

	@Override
	public Mono<Pais> save(Pais pais) {
		return paisRepository.save(pais);
	}

	@Override
	public Mono<Void> delete(Pais pais){
		return paisRepository.delete(pais);
	}

}
