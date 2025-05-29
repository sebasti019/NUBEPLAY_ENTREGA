package com.example.NUBEPLAY_INVENTARIO.controller;


import com.example.NUBEPLAY_INVENTARIO.model.InventarioModel;
import com.example.NUBEPLAY_INVENTARIO.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/juegos")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<InventarioModel> listarJuegos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer stock) {

        if (categoria != null) {
            return inventarioService.buscarCategoria(categoria);
        }
        if (stock != null) {
            return inventarioService.buscarStock(stock);
        }
        return inventarioService.getJuegos();
    }

    @PostMapping
    public InventarioModel agregarJuego(@RequestBody InventarioModel juego) {
        return inventarioService.guardarJuego(juego);
    }

    @GetMapping("{id}")
    public InventarioModel buscarJuego(@PathVariable int id) {
        return inventarioService.buscarJuego(id);
    }

    @PutMapping("{id}")
    public InventarioModel actualizarJuego(@PathVariable int id, @RequestBody InventarioModel juego) {
        juego.setIdJuego(id);
        return inventarioService.actualizarJuego(juego);
    }

    @DeleteMapping("{id}")
    public String eliminarJuego(@PathVariable int id) {
        return inventarioService.borrarJuego(id);
    }
}