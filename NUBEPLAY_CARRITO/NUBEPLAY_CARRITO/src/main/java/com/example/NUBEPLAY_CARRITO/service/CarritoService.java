package com.example.NUBEPLAY_CARRITO.service;

import com.example.NUBEPLAY_CARRITO.model.CarritoModel;
import com.example.NUBEPLAY_CARRITO.model.CarritoItemModel;
import com.example.NUBEPLAY_CARRITO.repository.CarritoRepository;
import com.example.NUBEPLAY_CARRITO.repository.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public CarritoModel crearCarrito(CarritoModel carrito) {
        return carritoRepository.save(carrito);
    }

    public CarritoItemModel agregarItem(CarritoItemModel item, int idCarrito) {
    Optional<CarritoModel> carritoOpt = carritoRepository.findById(idCarrito);
    if (carritoOpt.isEmpty()) {
        throw new RuntimeException("Carrito no encontrado");
    }

    item.setCarrito(carritoOpt.get());
    return carritoItemRepository.save(item);
}

    public List<CarritoItemModel> obtenerItems(int idCarrito) {
        return carritoItemRepository.findByCarrito_IdCarrito(idCarrito);
    }
}