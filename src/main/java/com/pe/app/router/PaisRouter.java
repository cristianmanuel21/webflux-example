package com.pe.app.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pe.app.handler.PaisHandler;

@Configuration
public class PaisRouter {
	
	private static final String PATH= "/api/v1/paises";
	
	@Bean
	RouterFunction<ServerResponse> paisRoutes(PaisHandler paisHandler){
		
		return RouterFunctions.route(RequestPredicates.GET(PATH),paisHandler::getAllPaises)
				.andRoute(RequestPredicates.GET(PATH+"/{id}"), paisHandler::getPaisById)
				.andRoute(RequestPredicates.POST(PATH), paisHandler::savePais)
				.andRoute(RequestPredicates.PUT(PATH+"/{id}"),paisHandler::updatePais)
				.andRoute(RequestPredicates.DELETE(PATH+"/{id}"), paisHandler::deletePais);
	}
	
}
