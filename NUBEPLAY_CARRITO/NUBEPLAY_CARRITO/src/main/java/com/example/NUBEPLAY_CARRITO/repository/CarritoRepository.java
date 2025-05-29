package com.example.NUBEPLAY_CARRITO.repository;

import com.example.NUBEPLAY_CARRITO.model.CarritoModel; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoModel, Integer> {
}