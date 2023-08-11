package com.pe.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.pe.app.documents.Pais;
import com.pe.app.documents.Persona;
import com.pe.app.documents.Role;
import com.pe.app.service.PaisService;
import com.pe.app.service.PersonaService;
import com.pe.app.service.UserServcice;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class DemoFinalApplication implements CommandLineRunner{

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private PersonaService personaService;
	
	@Autowired
	private UserServcice userService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(DemoFinalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("paises").subscribe();
		mongoTemplate.dropCollection("personas").subscribe();
		mongoTemplate.dropCollection("roles").subscribe();
		mongoTemplate.dropCollection("usuarios").subscribe();
		
		Role role=new Role();
		role.setNombre("ROLE_USER");
		
		Role role2=new Role();
		role2.setNombre("ROLE_ADMIN");
		
		Role role3=new Role();
		role3.setNombre("ROLE_MODERATOR");
		
		Flux.just(role,role2,role3)
		.flatMap(userService::saveRoles)
		.subscribe(r->{
				log.info("Rol creado: "+r.getNombre()+" Id: "+r.getId());
		});
		
		Pais pais1=new Pais("Peru");
		Pais pais2=new Pais("Brasil");
		Pais pais3=new Pais("Argentina");
		Pais pais4=new Pais("Uruguay");
		Pais pais5=new Pais("Bolivia");
		
		
		Flux.just(pais1, pais2, pais3, pais4,pais5)
		.flatMap(paisService::save)
		.doOnNext(c ->{
			log.info("Pais creada: " + c.getNombre() + ", Id: " + c.getId());
		}).thenMany(
				Flux.just(new Persona("11223344","Piero","Quispe",26,pais1),
						new Persona("11223300","Ronaldo","Nazario",45,pais2),
						new Persona("77223300","Lionel","Messi",35,pais3),
						new Persona("26193300","Edison","Cavani",34,pais4),
						new Persona("12563150","Evo","Morales",67,pais5)
				)
				.flatMap(p-> personaService.save(p))
		)
		.subscribe(r -> log.info("Insert Persona: " + r.getNombre() + " que reside en  " + r.getPais().getNombre()));
		
	}

}
