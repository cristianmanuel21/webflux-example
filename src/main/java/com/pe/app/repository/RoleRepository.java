package com.pe.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pe.app.documents.ERole;
import com.pe.app.documents.Role;

import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveMongoRepository<Role, String>{
	
	Mono<Role> findByNombre(ERole nombre);

}
