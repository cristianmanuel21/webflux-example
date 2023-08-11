package com.pe.app.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pe.app.documents.Pais;
import com.pe.app.documents.Persona;
import com.pe.app.service.PaisService;
import com.pe.app.service.PersonaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonaHandler {
	
	@Autowired
	private PersonaService personaService;
	
	@Autowired
	private Validator validator;
	
	
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getAllPersonas(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(personaService.findAllPersona(), Persona.class);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getById(ServerRequest request){
		String id=request.pathVariable("id");
		return personaService.findById(id)
				.flatMap(p-> ServerResponse.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.bodyValue(p))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getByDni(ServerRequest request){
		String dni=request.pathVariable("dni");
		return personaService.findByDNI(dni)
					.flatMap(d-> ServerResponse.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(d))
					.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> savePersona(ServerRequest request){
		Mono<Persona> personaReq=request.bodyToMono(Persona.class);
		
		return personaReq.flatMap(p-> {
				Errors errors=new BeanPropertyBindingResult(p, Persona.class.getName());
				validator.validate(p,errors);
			if(errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError -> "El campo "+fieldError.getField()+" "+fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list->ServerResponse.badRequest().bodyValue(list));
			}
			else {
				return personaService.save(p)
					.flatMap(k-> ServerResponse.created(URI.create("/api/v1/personas/".concat(k.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(k));
			}
		});
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> updatePersona(ServerRequest request){
		String dni=request.pathVariable("dni");
		Mono<Persona> personaReq=request.bodyToMono(Persona.class);
		Mono<Persona> personaDb=personaService.findByDNI(dni);
		
		return personaDb.zipWith(personaReq, (db,req)->{
				if(req.getNombre()!=null && !req.getNombre().isEmpty())db.setNombre(req.getNombre());
				if(req.getApellido()!=null && !req.getApellido().isEmpty())db.setApellido(req.getApellido());
				if(req.getPais()!=null)db.setPais(req.getPais());
				return db;
			}).flatMap(p-> ServerResponse.created(URI.create("/api/v1/personas/".concat(p.getId())))
					.contentType(MediaType.APPLICATION_JSON)
					.body(personaService.update(p),Persona.class));
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> deletePersona(ServerRequest request){
		String id=request.pathVariable("id");
		Mono<Persona> personaDb=personaService.findById(id);
		return personaDb.flatMap(p->personaService.delete(p)
				.then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	
	

}
