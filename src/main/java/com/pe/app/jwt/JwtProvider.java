package com.pe.app.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtProvider {
	
	public final static Key SECRET_KEY=Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	public String generateToken(UserDetails userDetails) {
		final String roles	= userDetails.getAuthorities()
								.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.joining(","));
	
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("roles",roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+expiration*1000))
				.signWith(SECRET_KEY)
				.compact();
	}
	
	public Claims getClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String getSubject(String token) {
		return this.getClaims(token).getSubject();
	}

}
