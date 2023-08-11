package com.pe.app.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.pe.app.handler.UserHandler;

@Configuration
public class UserAuth {
	
	private static final String PATH = "auth/";


    @Bean
    RouterFunction<ServerResponse> authRoutes(UserHandler handler) {
        return RouterFunctions.route()
                .POST(PATH + "login", handler::login)
                .POST(PATH + "create", handler::create)
                .build();
    }

}
