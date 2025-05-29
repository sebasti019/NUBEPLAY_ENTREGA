package com.example.NUBEPLAY_USUARIO.controller;

import java.util.List;
import java.util.Optional;


import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import com.example.NUBEPLAY_USUARIO.service.UsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public List<UsuarioModel> listarUsuarios(@RequestParam(required = false) String rol,
                                             @RequestParam(required = false) Boolean activo) {
        if (rol != null) return usuarioService.buscarPorRol(rol);
        if (activo != null) return usuarioService.buscarPorActivo(activo);
        return usuarioService.getUsuarios();
    }


    @PostMapping
    public ResponseEntity<UsuarioModel> agregarUsuario(@RequestBody UsuarioModel usuario) {
        UsuarioModel nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuario(@PathVariable int id) {
        Optional<UsuarioModel> usuarioOpt = usuarioService.buscarUsuario(id);
        return usuarioOpt
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> actualizarUsuario(@PathVariable int id, @RequestBody UsuarioModel usuario) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarUsuario(id);
        if (usuarioExistente.isPresent()) {
            usuario.setUserid(id);
            UsuarioModel usuarioActualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable int id) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarUsuario(id);
        if (usuarioExistente.isPresent()) {
            usuarioService.borrarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

