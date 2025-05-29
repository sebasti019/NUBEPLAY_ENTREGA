package com.example.NUBEPLAY_INVENTARIO.service;


import com.example.NUBEPLAY_INVENTARIO.model.InventarioModel;
import com.example.NUBEPLAY_INVENTARIO.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<InventarioModel> getJuegos() {
        return inventarioRepository.findAll();
    }

    public InventarioModel guardarJuego(InventarioModel juego) {
        return inventarioRepository.save(juego);
    }

    public InventarioModel buscarJuego(int id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    public InventarioModel actualizarJuego(InventarioModel juego) {
        return inventarioRepository.save(juego);
    }

    public String borrarJuego(int id) {
        inventarioRepository.deleteById(id); 
        return "juego eliminado";
    }

    public List<InventarioModel> buscarCategoria(String categoria) {
        return inventarioRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<InventarioModel> buscarStock(int stock) {
        return inventarioRepository.findByStockLessThan(stock);
    }
}