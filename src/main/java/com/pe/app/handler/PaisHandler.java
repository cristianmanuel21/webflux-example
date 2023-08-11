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
import com.pe.app.service.PaisService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PaisHandler{
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private Validator validator;
	
	
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getAllPaises(ServerRequest request){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(paisService.findAllPaises(), Pais.class);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getPaisById(ServerRequest request){
		String id= request.pathVariable("id");
		Mono<Pais> paisDb=paisService.findById(id);
		
		return paisDb.flatMap(db->ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(db))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> getByNombre(ServerRequest request){
		String nombre=request.pathVariable("nombre");
		Mono<Pais> paisdb=paisService.findByNombre(nombre);
		return paisdb.flatMap(db->ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(db))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> savePais(ServerRequest request){
		Mono<Pais> paisReq=request.bodyToMono(Pais.class);
		return paisReq.flatMap(rq-> {
				Errors errors=new BeanPropertyBindingResult(rq, Pais.class.getName());
				validator.validate(rq,errors);
				if(errors.hasErrors()) {
					return Flux.fromIterable(errors.getFieldErrors())
							.map(fieldError -> "El campo "+fieldError.getField()+" "+fieldError.getDefaultMessage())
							.collectList()
							.flatMap(list->ServerResponse.badRequest().bodyValue(list));
				} else {
					return paisService.save(rq)
							.flatMap(p-> ServerResponse.created(URI.create("/api/v1/paises/".concat(p.getId())))
									.contentType(MediaType.APPLICATION_JSON)
									.bodyValue(p));
				}
		});
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> updatePais(ServerRequest request){
		Mono<Pais> paisReq=request.bodyToMono(Pais.class);
		String id=request.pathVariable("id");
		Mono<Pais> paidDb=paisService.findById(id);
		
		return paidDb.zipWith(paisReq, (db,req)->{
			if(req.getNombre()!=null && !req.getNombre().isEmpty())db.setNombre(req.getNombre());
			return db;
		}).flatMap(p->ServerResponse.created(URI.create("/api/v1/paises/".concat(p.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(paisService.save(p), Pais.class));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ServerResponse> deletePais(ServerRequest request){
		String id=request.pathVariable("id");
		Mono<Pais> paisDb=paisService.findById(id);
		
		return paisDb.flatMap(p-> paisService.delete(p)
					 .then(ServerResponse.noContent().build()))
					 .switchIfEmpty(ServerResponse.notFound().build());
	}
	
}
