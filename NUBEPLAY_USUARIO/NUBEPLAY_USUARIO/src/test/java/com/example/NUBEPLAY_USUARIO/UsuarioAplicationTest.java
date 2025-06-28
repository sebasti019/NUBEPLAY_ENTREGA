package com.example.NUBEPLAY_USUARIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import com.example.NUBEPLAY_USUARIO.repository.UsuarioRepository;
import net.datafaker.Faker;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")

public class UsuarioAplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Faker faker = new Faker();
    private String baseUrl;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/usuarios";

        usuarioRepository.deleteAll();
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
    void testCrearUsuario() {

        UsuarioModel nuevoUsuario = generarUsuarioFalso();
        HttpEntity<UsuarioModel> request = new HttpEntity<>(nuevoUsuario);

        ResponseEntity<UsuarioModel> response = restTemplate.postForEntity(
            baseUrl,
            request,
            UsuarioModel.class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isNotNull();
        assertThat(response.getBody().getNombre()).isEqualTo(nuevoUsuario.getNombre());
    }

    @Test
    @Order(2)
    void testGetUsuarioById() {

        UsuarioModel usuarioGuardado = usuarioRepository.save(generarUsuarioFalso());
        Integer idUsuario = usuarioGuardado.getUserid();
        assertNotNull(idUsuario, "El ID del usuario no debería ser nulo después de guardarlo");

        ResponseEntity<UsuarioModel> response = restTemplate.getForEntity(
            baseUrl + "/" + idUsuario,
            UsuarioModel.class
        );

        System.out.println("el id probado es este = "+ idUsuario);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isEqualTo(idUsuario);
        assertThat(response.getBody().getNombre()).isEqualTo(usuarioGuardado.getNombre());
    }

    @Test
    @Order(3)
    void testUpdateUsuario() {
        // 1) Guardamos un usuario para tener un ID existente
        UsuarioModel usuarioOriginal = usuarioRepository.save(generarUsuarioFalso());
        Integer idUsuario = usuarioOriginal.getUserid();
        assertNotNull(idUsuario, "El ID del usuario no debería ser nulo");

        // 2) Preparamos los nuevos datos
        UsuarioModel datosActualizados = generarUsuarioFalso();
        datosActualizados.setNombre("Nombre Actualizado Correctamente");

        // 3) Cabeceras JSON obligatorias
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 4) Envío del PUT con HttpEntity
        HttpEntity<UsuarioModel> request = new HttpEntity<>(datosActualizados, headers);
        ResponseEntity<UsuarioModel> response = restTemplate.exchange(
            baseUrl + "/" + idUsuario,
            HttpMethod.PUT,
            request,
            UsuarioModel.class
        );

        // 5) Asserts finales
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isEqualTo(idUsuario);
        assertThat(response.getBody().getNombre())
            .isEqualTo("Nombre Actualizado Correctamente");
    }

    @Test
    @Order(4)
    void testDeleteUsuario() {
        UsuarioModel usuarioABorrar = usuarioRepository.save(generarUsuarioFalso());
        Integer idUsuario = usuarioABorrar.getUserid();
        assertNotNull(idUsuario, "El ID del usuario no debería ser nulo");

        restTemplate.delete(baseUrl + "/" + idUsuario);

        ResponseEntity<UsuarioModel> response = restTemplate.getForEntity(
            baseUrl + "/" + idUsuario,
            UsuarioModel.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    @Order(5)
    void testGetAllUsuarios() {
        usuarioRepository.save(generarUsuarioFalso());
        usuarioRepository.save(generarUsuarioFalso());
        ResponseEntity<UsuarioModel[]> response = restTemplate.getForEntity(baseUrl, UsuarioModel[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }
}