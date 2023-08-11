	package com.pe.app.jwt;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.pe.app.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager{
	
	private final JwtProvider jwtProvider;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication)
				.map(auth-> jwtProvider.getClaims(auth.getCredentials().toString()))
				.onErrorResume(e-> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "bad token")))
				.log()
				.map(claims->  new UsernamePasswordAuthenticationToken(claims.getSubject(),
									null,
									Arrays.stream(claims.get("roles").toString().split(","))
									.map(SimpleGrantedAuthority::new)
									.collect(Collectors.toList())
								)
				);
	}

}
