package com.example.NUBEPLAY_CARRITO.controller;

import com.example.NUBEPLAY_CARRITO.model.CarritoModel;
import com.example.NUBEPLAY_CARRITO.model.CarritoItemModel;
import com.example.NUBEPLAY_CARRITO.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carrito/")

public class CarritoController {


    @Autowired
    private CarritoService carritoService;

    @PostMapping
    public CarritoModel crearCarrito(@RequestBody CarritoModel carrito) {
        return carritoService.crearCarrito(carrito);
    }

    @PostMapping("{idCarrito}/agregar")
    public CarritoItemModel agregarProducto(@PathVariable int idCarrito,
                                        @RequestParam int idJuego,
                                        @RequestParam int cantidad) {
        CarritoItemModel item = new CarritoItemModel();
        item.setIdJuego(idJuego);
        item.setCantidad(cantidad);

        return carritoService.agregarItem(item, idCarrito);
    }
    @GetMapping("{idCarrito}/items")
    public List<CarritoItemModel> verCarrito(@PathVariable int idCarrito) {
        return carritoService.obtenerItems(idCarrito);
    }
    
}