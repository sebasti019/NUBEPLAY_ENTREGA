package com.example.NUBEPLAY_USUARIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        usuario.setPassword("PasswordValida123!");
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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isEqualTo(idUsuario);
    }

    @Test
    @Order(3)
    void testUpdateUsuario() {
        UsuarioModel usuarioOriginal = usuarioRepository.save(generarUsuarioFalso());
        Integer idUsuario = usuarioOriginal.getUserid();
        assertNotNull(idUsuario, "El ID del usuario no debería ser nulo");
        UsuarioModel datosActualizados = generarUsuarioFalso();
        datosActualizados.setNombre("Nombre Actualizado Correctamente");
        HttpEntity<UsuarioModel> request = new HttpEntity<>(datosActualizados);
        ResponseEntity<UsuarioModel> response = restTemplate.exchange(
                baseUrl + "/" + idUsuario,
                HttpMethod.PUT,
                request,
                UsuarioModel.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserid()).isEqualTo(idUsuario);
        assertThat(response.getBody().getNombre()).isEqualTo("Nombre Actualizado Correctamente");
    }

    @Test
    @Order(4)
    void testDeleteUsuario() {
        UsuarioModel usuarioABorrar = usuarioRepository.save(generarUsuarioFalso());
        Integer idUsuario = usuarioABorrar.getUserid();
        assertNotNull(idUsuario, "El ID del usuario no debería ser nulo");
        restTemplate.delete(baseUrl + "/" + idUsuario);
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/" + idUsuario,
                String.class
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

    @Test
    @Order(6)
    void testBuscarPorRol() {
        String rolBuscado = "admin";
        UsuarioModel usuarioAdmin = generarUsuarioFalso();
        usuarioAdmin.setRol(rolBuscado);
        usuarioRepository.save(usuarioAdmin);
        usuarioRepository.save(generarUsuarioFalso());
        ResponseEntity<UsuarioModel[]> response = restTemplate.getForEntity(
                baseUrl + "?rol=" + rolBuscado,
                UsuarioModel[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getRol()).isEqualTo(rolBuscado);
    }

    @Test
    @Order(7)
    void testBuscarPorActivo() {
        UsuarioModel usuarioActivo = generarUsuarioFalso();
        usuarioActivo.setActivo(true);
        usuarioRepository.save(usuarioActivo);
        UsuarioModel usuarioInactivo = generarUsuarioFalso();
        usuarioInactivo.setActivo(false);
        usuarioRepository.save(usuarioInactivo);
        ResponseEntity<UsuarioModel[]> response = restTemplate.getForEntity(
                baseUrl + "?activo=true",
                UsuarioModel[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].isActivo()).isTrue();
    }
}