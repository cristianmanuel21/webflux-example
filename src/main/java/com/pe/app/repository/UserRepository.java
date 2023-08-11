package com.pe.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pe.app.documents.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String>{
	
	Mono<User> findByUsername(String username);

}
