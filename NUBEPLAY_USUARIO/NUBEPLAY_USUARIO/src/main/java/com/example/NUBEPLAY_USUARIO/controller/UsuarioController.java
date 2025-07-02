package com.example.NUBEPLAY_USUARIO.controller;

import java.util.List;
import java.util.Optional;


import com.example.NUBEPLAY_USUARIO.model.UsuarioModel;
import com.example.NUBEPLAY_USUARIO.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones CRUD para la gestión de usuarios")
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios o filtra por rol o estado activo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
    })
 
    @GetMapping
    public List<UsuarioModel> listarUsuarios(@Parameter(description = "Filtrar por rol") @RequestParam(name = "rol" , required = false) String rol,
                                             @Parameter(description = "Filtrar por estado activo") @RequestParam(name = "activo" , required = false) Boolean activo) {
        if (rol != null) return usuarioService.buscarPorRol(rol);
        if (activo != null) return usuarioService.buscarPorActivo(activo);
        return usuarioService.getUsuarios();
    }

    @Operation(summary = "Agregar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @PostMapping
    public ResponseEntity<UsuarioModel> agregarUsuario(@RequestBody UsuarioModel usuario) {
        UsuarioModel nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }@Parameter(description = "ID del usuario")@Parameter(description = "ID del usuario")


    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuario(@Parameter(description = "ID del usuario") @PathVariable(value = "id") int id) {
        Optional<UsuarioModel> usuarioOpt = usuarioService.buscarUsuario(id);
        return usuarioOpt
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> actualizarUsuario(@Parameter(description = "ID del usuario") @PathVariable(value = "id") int id, @RequestBody UsuarioModel usuario) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarUsuario(id);
        if (usuarioExistente.isPresent()) {
            usuario.setUserid(id);
            UsuarioModel usuarioActualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@Parameter(description = "ID del usuario") @PathVariable(value = "id") int id) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarUsuario(id);
        if (usuarioExistente.isPresent()) {
            usuarioService.borrarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

