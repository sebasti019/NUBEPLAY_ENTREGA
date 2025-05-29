package com.example.NUBEPLAY_USUARIO.service;


import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import com.example.NUBEPLAY_USUARIO.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioModel> buscarPorRol(String rol) {
        return usuarioRepository.findByRolIgnoreCase(rol);
    }

    public List<UsuarioModel> buscarPorActivo(Boolean activo) {
        return usuarioRepository.findByActivo(activo);
    }

    public void borrarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioModel> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel guardarUsuario(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> buscarUsuario(int id) {
        return usuarioRepository.findById(id);
    }
}