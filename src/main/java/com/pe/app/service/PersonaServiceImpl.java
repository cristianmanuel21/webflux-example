package com.pe.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pe.app.documents.Pais;
import com.pe.app.documents.Persona;
import com.pe.app.exception.CustomException;
import com.pe.app.repository.PaisRepository;
import com.pe.app.repository.PersonaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonaServiceImpl implements PersonaService{
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private PaisRepository paisRepository;

	@Override
	public Flux<Persona> findAllPersona() {
		return personaRepository.findAll();
	}

	@Override
	public Mono<Persona> findById(String id) {
		return personaRepository.findById(id);
	}

	@Override
	public Mono<Persona> findByDNI(String dni) {
		return personaRepository.findByDni(dni);
	}

	@Override
	public Mono<Persona> save(Persona persona) {
		Mono<Pais> paisMono=  paisRepository.findByNombre(persona.getPais().getNombre());
		return paisMono
			    .filter(pa -> pa != null)
			    .flatMap(pa -> {
			        persona.setPais(pa);
			        return personaRepository.save(persona);
			    })
			    .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "El país no existe")));
	}

	@Override
	public Mono<Void> delete(Persona persona) {
		return personaRepository.delete(persona);
	}

	@Override
	public Mono<Persona> update(Persona persona) {
		return personaRepository.save(persona);
	}

}
