package com.pe.app.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pe.app.handler.PersonaHandler;



@Configuration
public class PersonaRouter {
	
	private static final String PATH= "/api/v1/personas";
	
	@Bean
	RouterFunction<ServerResponse> personaRoutes(PersonaHandler personaHandler){
		return RouterFunctions.route()
				.GET(PATH , personaHandler::getAllPersonas)
				.GET(PATH+"/dni/{dni}",personaHandler::getByDni)
				.GET(PATH+"/{id}",personaHandler::getById)
				.POST(PATH,personaHandler::savePersona)
				.PUT(PATH+"/{dni}",personaHandler::updatePersona)
				.DELETE(PATH+"/{id}",personaHandler::deletePersona)
				.build();
	}

}
