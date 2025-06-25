package com.example.NUBEPLAY_USUARIO;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.NUBEPLAY_USUARIO.controller.UsuarioController;
import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;

import net.datafaker.Faker;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioAplicationTest {

    @Autowired
    private UsuarioController usuarioController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final Faker faker = new Faker();
    private static Integer testUsuarioId;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    private UsuarioModel generarUsuarioFalso() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(faker.name().fullName());
        usuario.setCorreo(faker.internet().emailAddress());
        usuario.setPassword("Password123!");
        usuario.setRol("cliente");
        usuario.setActivo(true);
        return usuario;
    }

    @Test
    @Order(1)
    void contextLoads() {
        System.out.println("Testing the context loading...");
        // System.out.println("Server running on port: " + port);
    }

    @Test
    @Order(2)
    void contextLoads2() throws Exception {
        System.out.println("Testing the context loading. and the controller...");
        assertThat(usuarioController).isNotNull();
    }

    @Test
    @Order(3)
    void getUsuarioContainsBrackets() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                "/api/v1/usuarios",
                String.class)).toString().contains("[");
    }


    @Test
    @Order(4)
    void testCrearUsuario() {
        UsuarioModel nuevoUsuario = generarUsuarioFalso();

        ResponseEntity<UsuarioModel> response = restTemplate.postForEntity(
                getUrl("/api/v1/usuarios"),
                nuevoUsuario,
                UsuarioModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        testUsuarioId = response.getBody().getUserid(); // guardar ID para otros tests
        assertThat(testUsuarioId).isNotNull();
        System.out.println("Usuario creado con ID: " + testUsuarioId);
    }

    @Test
    @Order(5)
    void testGetUsuarioById() {
        assertThat(testUsuarioId).isNotNull();

        ResponseEntity<UsuarioModel> response = restTemplate.getForEntity(
                getUrl("/api/v1/usuarios/" + testUsuarioId),
                UsuarioModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isEqualTo(testUsuarioId);
    }

    @Test
    @Order(6)
    void testUpdateUsuario() {
        assertThat(testUsuarioId).isNotNull();

        UsuarioModel actualizado = generarUsuarioFalso();
        actualizado.setUserid(testUsuarioId); // mismo ID

        HttpEntity<UsuarioModel> request = new HttpEntity<>(actualizado);
        restTemplate.put(getUrl("/api/v1/usuarios/" + testUsuarioId), request);

        ResponseEntity<UsuarioModel> response = restTemplate.getForEntity(
                getUrl("/api/v1/usuarios/" + testUsuarioId),
                UsuarioModel.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNombre()).isEqualTo(actualizado.getNombre());
    }

    @Test
    @Order(7)
    void testGetAllUsuarios() {
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/v1/usuarios"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[");
    }

    @Test
    @Order(8)
    void testDeleteUsuario() {
        assertThat(testUsuarioId).isNotNull();

        restTemplate.delete(getUrl("/api/v1/usuarios/" + testUsuarioId));

        ResponseEntity<UsuarioModel> response = restTemplate.getForEntity(
                getUrl("/api/v1/usuarios/" + testUsuarioId),
                UsuarioModel.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  
}
