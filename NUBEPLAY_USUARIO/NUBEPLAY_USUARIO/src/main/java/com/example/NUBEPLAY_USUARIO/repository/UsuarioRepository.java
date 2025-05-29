package com.example.NUBEPLAY_USUARIO.repository;

import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    List<UsuarioModel> findByRolIgnoreCase(String rol);
    List<UsuarioModel> findByActivo(boolean activo);
}