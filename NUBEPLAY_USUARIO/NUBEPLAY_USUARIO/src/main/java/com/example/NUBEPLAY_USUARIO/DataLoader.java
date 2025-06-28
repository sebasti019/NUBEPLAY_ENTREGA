package com.example.NUBEPLAY_USUARIO;

import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import com.example.NUBEPLAY_USUARIO.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
//
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            UsuarioModel usuario = new UsuarioModel();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setPassword(faker.internet().password());
            usuario.setRol(random.nextBoolean()?"cliente":"admin");
            usuario.setActivo(random.nextBoolean());

            try {
                usuarioRepository.save(usuario);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                System.err.println("Data integrity violation occurred: " + e.getMessage());
            }
        }
    }
}
