package com.pe.app.security;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


import com.pe.app.jwt.JwtFilter;
import com.pe.app.repository.SecurityContextRepository;

import lombok.RequiredArgsConstructor;


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityPrincipal {
	
	private final SecurityContextRepository securityContextRepository;
	
	
	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http,JwtFilter jwtFilter) {
		 return http
				.authorizeExchange()
				.pathMatchers("/auth/**").permitAll()
				.anyExchange().authenticated()
				.and()
				.addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
				.securityContextRepository(securityContextRepository)
				.formLogin().disable()
				.logout().disable()
				.httpBasic().disable()
				.csrf().disable()
				.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	WebProperties.Resources resources() {
	    return new WebProperties.Resources();
	}

}
