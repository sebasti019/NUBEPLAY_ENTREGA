package com.example.NUBEPLAY_USUARIO;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.NUBEPLAY_USUARIO.controller.UsuarioController;
import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;

import net.datafaker.Faker;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioAplicationTest {

    @Autowired
    private UsuarioController usuarioController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
    void postUsuario() throws Exception {
    Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            UsuarioModel usuario = new UsuarioModel();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setPassword(faker.internet().password());
            usuario.setRol(random.nextBoolean()?"cliente":"admin");
            usuario.setActivo(random.nextBoolean());
        }
    }

    void getUsuarios() throws Exception {
        System.out.println("port: " + port);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                "/api/v1/libros",
                String.class)).toString().contains("Test4");
    }
  
}
