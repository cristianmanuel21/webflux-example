package com.pe.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pe.app.documents.ERole;
import com.pe.app.documents.Role;
import com.pe.app.documents.User;
import com.pe.app.dto.CreateUserDto;
import com.pe.app.dto.LoginDto;
import com.pe.app.dto.TokenDto;
import com.pe.app.exception.CustomException;
import com.pe.app.jwt.JwtProvider;
import com.pe.app.repository.RoleRepository;
import com.pe.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServcice{
	
	private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    
   
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<TokenDto> login(LoginDto dto) {
		return userRepository.findByUsername(dto.getUsername())
				.filter(user-> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
				.map(user->{
					return new TokenDto(jwtProvider.generateToken(user));
				}).switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")));
	}

	@Override
	public Mono<User> create(CreateUserDto dto) {
		List<Role> roles=new ArrayList<>();	
		if(dto.getRoles()==null) {
			Mono<Role> roles0=roleRepository.findByNombre(ERole.ROLE_USER);
			roles0.subscribe(roles::add);
		}else {
			dto.getRoles().forEach(role->{
				switch(role) {
					case "admin":
						Mono<Role> roles0=roleRepository.findByNombre(ERole.ROLE_ADMIN);
						roles0.subscribe(r->roles.add(r));
						break;
					case "mod":
						Mono<Role> roles1=roleRepository.findByNombre(ERole.ROLE_MODERATOR);
						roles1.subscribe(r1->roles.add(r1));
						break;
					default:
						Mono<Role> roles2=roleRepository.findByNombre(ERole.ROLE_USER);
						roles2.subscribe(r2->roles.add(r2));
						break;
				}
			});
		}
		
		User user=User.builder()
				.username(dto.getUsername())
				.email(dto.getEmail())
				.password(passwordEncoder.encode(dto.getPassword()))
				.roles(roles)
				.build();
		Mono<Boolean> userExists= userRepository.findByUsername(user.getUsername()).hasElement();
		return userExists
				.flatMap(exists -> exists ?
						Mono.error(new CustomException(HttpStatus.CONFLICT, "username already in use"))
						: userRepository.save(user));
	}

	@Override
	public Mono<Role> saveRoles(Role role) {
		return roleRepository.save(role);
	}

}
