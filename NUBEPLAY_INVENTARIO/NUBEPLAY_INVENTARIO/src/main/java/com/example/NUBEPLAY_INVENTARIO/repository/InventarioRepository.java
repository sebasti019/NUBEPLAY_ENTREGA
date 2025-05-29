package com.example.NUBEPLAY_INVENTARIO.repository;

import com.example.NUBEPLAY_INVENTARIO.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioModel, Integer> {
    List<InventarioModel> findByCategoriaIgnoreCase(String categoria);
    List<InventarioModel> findByStockLessThan(int stock);
}