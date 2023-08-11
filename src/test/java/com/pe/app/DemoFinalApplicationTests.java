package com.pe.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.pe.app.documents.Pais;
import com.pe.app.documents.Persona;
import com.pe.app.service.PaisService;
import com.pe.app.service.PersonaService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoFinalApplicationTests {

	//crear pais, actualizar nombre, eliminar pais
	//crear persona, buscarpordni, actualizar nombre, eliminar Persona
	// agregar el order al final
	
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private PersonaService personaService;
	
	

	@Test
	@Order(1)
	public void listarTest() {
		client.get()
		.uri("/api/v1/paises")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(Pais.class)
		.consumeWith(response->{
			List<Pais> paises=response.getResponseBody();
			Assertions.assertThat(paises.size()>0).isTrue();
		});
	}
	
	
	@Test
	@Order(2)
	public void getByName() {
		Pais pais=paisService.findByNombre("Peru").block();
		client.get()
		.uri("/api/v1/paises/{id}",Collections.singletonMap("id", pais.getId()))
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Peru");
	}
	
	
	@Test
	@Order(3)
	public void createPais() {
		Pais pais=new Pais("Venesuela");
		client.post()
		.uri("/api/v1/paises")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.bodyValue(pais)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Pais.class)
		.consumeWith(response->{
			Pais p=response.getResponseBody();
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Venesuela");
		});
	}
	
	@Test
	@Order(4)
	public void actualizaPais() {
		Pais pais=paisService.findByNombre("Venesuela").block();
		Pais paisEditado=new Pais("Venezuela");
		
		client.put()
		.uri("/api/v1/paises/{id}", pais.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(paisEditado),Pais.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Venezuela");
	}
	
	@Test
	@Order(5)
	public void deletePais() {
		Pais pais=paisService.findByNombre("Venezuela").block();
		client.delete()
		.uri("/api/v1/paises/{id}", pais.getId())
		.exchange()
		.expectStatus()
		.isNoContent()
		.expectBody()
		.isEmpty();
		
		client.get()
		.uri("/api/v1/paises/{id}",Collections.singletonMap("id", pais.getId()))
		.exchange()
		.expectStatus()
		.isNotFound()
		.expectBody()
		.isEmpty();
	}
	
	@Test
	@Order(6)
	public void createPersona() {
		Pais pais=paisService.findByNombre("Brasil").block();
		Persona persona=new Persona("11335522","Marcelo" ,"Souza",34, pais);
		client.post()
		.uri("/api/v1/personas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.bodyValue(persona)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Marcelo")
		.jsonPath("$.dni").isEqualTo("11335522")
		.jsonPath("$.pais.nombre").isEqualTo("Brasil");
	}
	
	@Test
	@Order(7)
	public void buscarPersona() {
		Persona persona=personaService.findByDNI("11335522").block();
		client.get()
		.uri("/api/v1/personas/dni/{dni}",persona.getDni())
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Marcelo")
		.jsonPath(".pais.nombre").isEqualTo("Brasil");
	}
	
	@Test
	@Order(8)
	public void actualizarPersona() {
		Persona persona=personaService.findByDNI("11335522").block();
		Persona personaActualizada=new Persona();
		personaActualizada.setNombre("Marcelino");
		client.put()
		.uri("/api/v1/personas/{id}",Collections.singletonMap("id", persona.getDni()))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.bodyValue(personaActualizada)
		.exchange()
		.expectStatus().isCreated()
		.expectBody(Persona.class)
		.consumeWith(response->{
			Persona per=response.getResponseBody();
			Assertions.assertThat(per.getId()).isNotEmpty();
			Assertions.assertThat(per.getNombre()).isEqualTo("Marcelino");
		});
	}
	
	@Test
	@Order(9)
	public void eliminarPersona() {
		Persona persona=personaService.findByDNI("11335522").block();
		client.delete()
		.uri("/api/v1/personas/{id}",persona.getId())
		.exchange()
		.expectStatus()
		.isNoContent()
		.expectBody()
		.isEmpty();
		
		client.get()
		.uri("/api/v1/personas/{id}",persona.getId())
		.exchange()
		.expectStatus()
		.isNotFound()
		.expectBody()
		.isEmpty();
	}
	
	
	
	
	
	
	
	
	

}
