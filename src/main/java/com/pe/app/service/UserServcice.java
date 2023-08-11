package com.pe.app.service;

import com.pe.app.documents.Role;
import com.pe.app.documents.User;
import com.pe.app.dto.CreateUserDto;
import com.pe.app.dto.LoginDto;
import com.pe.app.dto.TokenDto;

import reactor.core.publisher.Mono;

public interface UserServcice {
	
	Mono<TokenDto> login(LoginDto dto);
	Mono<User> create(CreateUserDto dto);
	Mono<Role> saveRoles(Role role);

}
