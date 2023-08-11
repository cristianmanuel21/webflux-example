package com.pe.app.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pe.app.documents.User;
import com.pe.app.dto.CreateUserDto;
import com.pe.app.dto.LoginDto;
import com.pe.app.dto.TokenDto;
import com.pe.app.service.UserServcice;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
	
	private final UserServcice userService;
	
	 public Mono<ServerResponse> login(ServerRequest request) {
	        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class);
	        return dtoMono
	                .flatMap(dto -> ServerResponse.ok()
	                			.contentType(MediaType.APPLICATION_JSON)
	                			.body(userService.login(dto), TokenDto.class));
	 }

	 public Mono<ServerResponse> create(ServerRequest request) {
	        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class);
	        return dtoMono
	                .flatMap(dto -> ServerResponse.ok()
	                		.contentType(MediaType.APPLICATION_JSON)
	                		.body(userService.create(dto),User.class));
	    }

}
